import React from 'react';
import PropTypes from 'prop-types';
import Collapse, { Panel } from '../../../../snippets/Collapse/Collapse';

function CollapseFieldSet({ render, rows, type, label, expand, hasArrow }) {
  return (
    <Collapse defaultActiveKey={expand ? '0' : null}>
      <Panel header={label} type={type} showArrow={hasArrow} forceRender>
        {render(rows)}
      </Panel>
    </Collapse>
  );
}

CollapseFieldSet.propTypes = {
  rows: PropTypes.array,
  type: PropTypes.string,
  label: PropTypes.string,
  expand: PropTypes.bool,
  hasArrow: PropTypes.bool,
  render: PropTypes.func,
};

CollapseFieldSet.defaultProps = {
  rows: [],
  hasArrow: true,
};

export default CollapseFieldSet;
