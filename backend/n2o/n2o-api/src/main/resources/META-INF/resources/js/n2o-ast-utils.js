// n2o-ast-utils.js
// Утилиты для статического анализа JS-выражений.
// Зависимость: acorn.js должен быть загружен раньше.

function n2oNodeToSource(node) {
    if (node.type === 'Identifier') return node.name;
    if (node.type === 'MemberExpression') {
        const obj = n2oNodeToSource(node.object);
        return node.computed
            ? obj + '[' + n2oNodeToSource(node.property) + ']'
            : obj + '.' + node.property.name;
    }
    if (node.type === 'Literal') return String(node.value);
    return '';
}

function n2oGetRootName(node) {
    if (node.type === 'Identifier') return node.name;
    if (node.type === 'MemberExpression') return n2oGetRootName(node.object);
    return null;
}

function n2oGetRootStart(node) {
    if (node.type === 'Identifier') return node.start;
    if (node.type === 'MemberExpression') return n2oGetRootStart(node.object);
    return node.start;
}

function n2oWalkChildren(node, walkFn) {
    for (const key of Object.keys(node)) {
        if (key === 'type' || key === 'start' || key === 'end') continue;
        const child = node[key];
        if (Array.isArray(child)) {
            child.forEach(function(item) { walkFn(item); });
        } else if (child && typeof child === 'object' && child.type) {
            walkFn(child);
        }
    }
}

function n2oExtractVars(script) {
    const ast = acorn.parse(script, { ecmaVersion: 2020 });
    const names = [];
    const boundVars = {};

    function walk(node) {
        if (!node || typeof node !== 'object') return;
        if (node.type === 'ArrowFunctionExpression' || node.type === 'FunctionExpression' || node.type === 'FunctionDeclaration') {
            const bound = [];
            (node.params || []).forEach(function(param) {
                if (param.type === 'Identifier') bound.push(param.name);
            });
            bound.forEach(function(name) { boundVars[name] = (boundVars[name] || 0) + 1; });
            walk(node.body);
            bound.forEach(function(name) { boundVars[name]--; if (!boundVars[name]) delete boundVars[name]; });
            return;
        }
        if (node.type === 'CallExpression') {
            const callee = node.callee;
            if (callee.type === 'MemberExpression') {
                walk(callee.object);
            } else {
                walk(callee);
            }
            (node.arguments || []).forEach(walk);
            return;
        }
        if (node.type === 'MemberExpression') {
            const src = n2oNodeToSource(node);
            const root = n2oGetRootName(node);
            if (src && root && !boundVars[root]) names.push(src);
            return;
        }
        if (node.type === 'Identifier') {
            if (!boundVars[node.name]) names.push(node.name);
            return;
        }
        n2oWalkChildren(node, walk);
    }

    walk(ast);
    return names;
}

function n2oExtractRootVars(script) {
    const ast = acorn.parse(script, { ecmaVersion: 2020 });
    const roots = {};

    function walk(node) {
        if (!node || typeof node !== 'object') return;
        if (node.type === 'MemberExpression') {
            const root = n2oGetRootName(node);
            if (root) roots[root] = true;
            return;
        }
        if (node.type === 'Identifier') {
            roots[node.name] = true;
            return;
        }
        n2oWalkChildren(node, walk);
    }

    walk(ast);
    return Object.keys(roots);
}

function n2oAddContextFor(script, context, vars) {
    const ast = acorn.parse(script, { ecmaVersion: 2020 });
    const varsSet = {};
    for (const v of vars) {
        varsSet[String(v)] = true;
    }

    const replacements = [];

    function walk(node) {
        if (!node || typeof node !== 'object') return;
        if (node.type === 'MemberExpression') {
            const root = n2oGetRootName(node);
            if (root && varsSet[root]) {
                replacements.push({ pos: n2oGetRootStart(node), text: context + '.' });
            }
            return;
        }
        if (node.type === 'Identifier') {
            if (varsSet[node.name]) {
                replacements.push({ pos: node.start, text: context + '.' });
            }
            return;
        }
        n2oWalkChildren(node, walk);
    }

    walk(ast);

    replacements.sort(function(a, b) { return b.pos - a.pos; });
    let result = script;
    for (const r of replacements) {
        result = result.slice(0, r.pos) + r.text + result.slice(r.pos);
    }
    return result;
}
