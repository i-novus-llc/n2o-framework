#!/usr/bin/env python3
"""
N2O XML Validator — checks generated XML for common mistakes.
Usage: python3 validate_n2o.py <file.xml> [<file2.xml> ...]
       python3 validate_n2o.py --dir <directory>
Exit code: 0 = OK (warnings only), 1 = errors found
"""

import sys
import os
import re
import xml.etree.ElementTree as ET
from pathlib import Path

class Issue:
    def __init__(self, severity, message, line=None):
        self.severity = severity  # ERROR or WARN
        self.message = message
        self.line = line

    def __str__(self):
        loc = f" (line ~{self.line})" if self.line else ""
        return f"  [{self.severity}]{loc} {self.message}"


def find_line(content, search):
    """Approximate line number for a pattern."""
    for i, line in enumerate(content.splitlines(), 1):
        if search in line:
            return i
    return None


def validate_file(filepath):
    issues = []
    content = Path(filepath).read_text(encoding='utf-8')
    fname = os.path.basename(filepath)

    # 1. Parse XML
    try:
        # Strip namespace for easier traversal
        content_no_ns = re.sub(r'\sxmlns[^"]*"[^"]*"', '', content)
        root = ET.fromstring(content_no_ns)
    except ET.ParseError as e:
        issues.append(Issue("ERROR", f"XML parse error: {e}"))
        # Check common XML escaping issues
        if '&&' in content and '&amp;&amp;' not in content:
            issues.append(Issue("ERROR", "&& must be escaped as &amp;&amp; in XML",
                                find_line(content, '&&')))
        for bad, fix in [(' < ', ' &lt; '), (' > ', ' &gt; ')]:
            if bad in content:
                issues.append(Issue("ERROR", f"'{bad.strip()}' in text/attributes must be escaped as '{fix.strip()}'",
                                    find_line(content, bad)))
        return issues

    tag = root.tag

    # 2. Schema namespace check
    ns_match = re.search(r'xmlns="([^"]+)"', content)
    if ns_match:
        ns = ns_match.group(1)
        schema_map = {
            'page': 'page-4.0', 'simple-page': 'page-4.0', 'search-page': 'page-4.0',
            'left-right-page': 'page-4.0', 'top-left-right-page': 'page-4.0',
            'query': 'query-5.0',
            'object': 'object-4.0',
            'application': 'application-3.0',
        }
        expected = schema_map.get(tag)
        if expected and expected not in ns:
            old_ver = re.search(r'(\w+-[\d.]+)', ns)
            old = old_ver.group(1) if old_ver else ns
            issues.append(Issue("WARN", f"Schema '{old}' may be outdated. Latest is '{expected}'"))
    else:
        issues.append(Issue("WARN", "No xmlns namespace found on root element"))

    # 3. File naming check
    if fname.endswith('.page.xml') and tag not in ('page', 'simple-page', 'search-page',
                                                     'left-right-page', 'top-left-right-page'):
        issues.append(Issue("WARN", f"File named *.page.xml but root element is <{tag}>"))
    if fname.endswith('.query.xml') and tag != 'query':
        issues.append(Issue("WARN", f"File named *.query.xml but root element is <{tag}>"))
    if fname.endswith('.object.xml') and tag != 'object':
        issues.append(Issue("WARN", f"File named *.object.xml but root element is <{tag}>"))

    # 4. Query-specific checks
    if tag == 'query':
        # Must have field id="id"
        fields = root.findall('.//field')
        field_ids = [f.get('id') for f in fields]
        if 'id' not in field_ids:
            issues.append(Issue("ERROR", "Query MUST have <field id=\"id\">. Framework requires it.",
                                find_line(content, '<fields')))

        # Fields without expression (SQL queries)
        has_sql = root.find('.//sql') is not None
        if has_sql:
            for f in fields:
                fid = f.get('id', '?')
                if not f.get('expression') and fid != 'id':
                    issues.append(Issue("WARN", f"Field '{fid}' has no expression= attribute (needed for SQL :select)",
                                        find_line(content, f'id="{fid}"')))

        # Check :select, :filters, :limit, :offset in SQL list
        for elem_name in ['list']:
            elem = root.find(f'.//{elem_name}')
            if elem is not None:
                sql_elem = elem.find('sql')
                if sql_elem is not None and sql_elem.text:
                    sql_text = sql_elem.text
                    if ':select' not in sql_text:
                        issues.append(Issue("WARN", f"<{elem_name}> SQL missing :select placeholder",
                                            find_line(content, sql_text[:40])))
                    if ':filters' not in sql_text:
                        issues.append(Issue("WARN", f"<{elem_name}> SQL missing :filters placeholder",
                                            find_line(content, sql_text[:40])))
                    if ':limit' not in sql_text:
                        issues.append(Issue("WARN", f"<{elem_name}> SQL missing :limit placeholder",
                                            find_line(content, sql_text[:40])))

        # Check count has :filters
        count_elem = root.find('.//count')
        if count_elem is not None:
            sql_elem = count_elem.find('sql')
            if sql_elem is not None and sql_elem.text and ':filters' not in sql_elem.text:
                issues.append(Issue("WARN", "<count> SQL missing :filters placeholder"))

        # Filters referencing non-existent fields
        for filt in root.findall('.//filters/filter'):
            fid = filt.get('field-id', '')
            if fid and fid not in field_ids:
                issues.append(Issue("ERROR", f"Filter references field-id='{fid}' but no such <field> exists",
                                    find_line(content, f'field-id="{fid}"')))

    # 5. Object-specific checks
    if tag == 'object':
        for op in root.findall('.//operation'):
            op_id = op.get('id', '?')
            invocation = op.find('invocation')
            if invocation is None:
                issues.append(Issue("ERROR", f"Operation '{op_id}' missing <invocation>",
                                    find_line(content, f'id="{op_id}"')))
            # Check SQL mapping syntax
            for field in op.findall('.//in/field'):
                mapping = field.get('mapping', '')
                if mapping and not mapping.startswith('['):
                    issues.append(Issue("WARN",
                        f"Operation '{op_id}', field '{field.get('id')}': mapping should use ['name'] syntax, got '{mapping}'",
                        find_line(content, f'mapping="{mapping}"')))

    # 6. Page-specific checks
    if tag in ('page', 'simple-page', 'search-page', 'left-right-page', 'top-left-right-page'):
        # Datasource references
        ds_ids = set()
        for ds in root.findall('.//datasources/datasource'):
            did = ds.get('id')
            if did:
                ds_ids.add(did)
        for ds in root.findall('.//datasources/app-datasource'):
            did = ds.get('id')
            if did:
                ds_ids.add(did)
        for ds in root.findall('.//datasources/inherited-datasource'):
            did = ds.get('id')
            if did:
                ds_ids.add(did)

        # Check widget datasource references (only for <page>, not simple-page)
        if tag == 'page' and ds_ids:
            for widget_tag in ('table', 'form', 'list', 'cards', 'tiles', 'tree', 'calendar', 'chart', 'html', 'widget'):
                for w in root.findall(f'.//{widget_tag}'):
                    ds_ref = w.get('datasource')
                    if ds_ref and ds_ref not in ds_ids:
                        issues.append(Issue("ERROR",
                            f"<{widget_tag}> references datasource='{ds_ref}' but no such datasource defined. Available: {ds_ids}",
                            find_line(content, f'datasource="{ds_ref}"')))

        # Check invoke operation-id exists on buttons (can't verify cross-file, but check format)
        for invoke in root.findall('.//invoke'):
            if not invoke.get('operation-id'):
                issues.append(Issue("ERROR", "<invoke> missing operation-id attribute",
                                    find_line(content, '<invoke')))

        # Check show-modal/open-page have page-id
        for action_tag in ('show-modal', 'open-page', 'open-drawer'):
            for elem in root.findall(f'.//{action_tag}'):
                if not elem.get('page-id'):
                    issues.append(Issue("ERROR", f"<{action_tag}> missing page-id attribute",
                                        find_line(content, f'<{action_tag}')))

    # 7. General checks
    # Unescaped ampersands (already caught by parser, but check in CDATA-free zones)
    lines = content.splitlines()
    for i, line in enumerate(lines, 1):
        # Skip CDATA sections
        if '<![CDATA[' in line:
            continue
        # Find & not followed by amp; lt; gt; quot; apos; #
        unescaped = re.findall(r'&(?!amp;|lt;|gt;|quot;|apos;|#\d+;|#x[\da-fA-F]+;)', line)
        if unescaped:
            issues.append(Issue("WARN", f"Possible unescaped '&' in XML", i))

    # Check for common wrong attribute patterns
    wrong_patterns = [
        (r'query-id="[^"]*\.query\.xml"', "query-id should be just the id, not filename (remove .query.xml)"),
        (r'object-id="[^"]*\.object\.xml"', "object-id should be just the id, not filename (remove .object.xml)"),
        (r'page-id="[^"]*\.page\.xml"', "page-id should be just the id, not filename (remove .page.xml)"),
    ]
    for pattern, msg in wrong_patterns:
        m = re.search(pattern, content)
        if m:
            issues.append(Issue("ERROR", msg, find_line(content, m.group(0))))

    # Duplicate ids in same scope
    seen_ids = {}
    for elem in root.iter():
        eid = elem.get('id')
        if eid and elem.tag in ('field', 'datasource', 'column', 'tab', 'operation'):
            key = f"{elem.tag}:{eid}"
            if key in seen_ids:
                issues.append(Issue("WARN", f"Duplicate {elem.tag} id='{eid}'",
                                    find_line(content, f'id="{eid}"')))
            seen_ids[key] = True

    return issues


def main():
    files = []
    if len(sys.argv) < 2:
        print("Usage: python3 validate_n2o.py <file.xml> [...]")
        print("       python3 validate_n2o.py --dir <directory>")
        sys.exit(1)

    if sys.argv[1] == '--dir':
        directory = sys.argv[2] if len(sys.argv) > 2 else '.'
        for root, dirs, fnames in os.walk(directory):
            for fn in fnames:
                if fn.endswith('.xml'):
                    files.append(os.path.join(root, fn))
    else:
        files = sys.argv[1:]

    if not files:
        print("No XML files found.")
        sys.exit(0)

    total_errors = 0
    total_warnings = 0

    for filepath in sorted(files):
        if not os.path.isfile(filepath):
            print(f"⚠ File not found: {filepath}")
            continue

        issues = validate_file(filepath)
        errors = [i for i in issues if i.severity == "ERROR"]
        warnings = [i for i in issues if i.severity == "WARN"]
        total_errors += len(errors)
        total_warnings += len(warnings)

        if issues:
            status = "❌" if errors else "⚠"
            print(f"\n{status} {filepath} ({len(errors)} errors, {len(warnings)} warnings)")
            for issue in issues:
                print(issue)
        else:
            print(f"✅ {filepath} — OK")

    print(f"\n{'='*50}")
    print(f"Total: {len(files)} files, {total_errors} errors, {total_warnings} warnings")

    sys.exit(1 if total_errors > 0 else 0)


if __name__ == '__main__':
    main()
