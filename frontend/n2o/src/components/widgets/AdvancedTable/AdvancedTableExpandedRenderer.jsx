import React from 'react';

function AdvancedTableExpandedRenderer(record, index, indent, expanded) {
  const { expandedContent, children } = record;

  if (expandedContent) {
    if (expandedContent.type === 'html') {
      return <div dangerouslySetInnerHTML={{ __html: expandedContent.value }} />;
    }
    return <div>{expandedContent.value}</div>;
  }
  return <div />;
}

export default AdvancedTableExpandedRenderer;
