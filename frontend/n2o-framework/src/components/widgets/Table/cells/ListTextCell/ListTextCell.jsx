import React from 'react';
import PropTypes from 'prop-types';
import Tooltip from './Tooltip';
import { compose, setDisplayName, withHandlers } from 'recompose';
import withCell from '../../withCell';

const ListTextCell = props => {
  return (
    <div className="list-text-cell">
      <Tooltip
        tooltipList={['first', 'second', 'very looooooong name']}
        {...props}
      />
    </div>
  );
};

export { ListTextCell };
export default compose(
  setDisplayName('ListTextCell'),
  withCell,
  withHandlers({
    onClick: ({ callAction, model }) => () => {
      callAction(model);
    },
  })
)(ListTextCell);
