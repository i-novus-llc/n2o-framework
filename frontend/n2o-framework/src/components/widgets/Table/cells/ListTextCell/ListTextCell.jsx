import React from 'react';
import PropTypes from 'prop-types';
import Tooltip from './Tooltip';
import { compose, setDisplayName } from 'recompose';
import withCell from '../../withCell';

function ListTextCell(props) {
  return (
    <div className="list-text-cell">
      <Tooltip {...props} />
    </div>
  );
}

export { ListTextCell };
export default compose(
  setDisplayName('ListTextCell'),
  withCell
)(ListTextCell);

ListTextCell.propTypes = {
  /**
   * ID ячейки
   */
  id: PropTypes.string.isRequired,
  /**
   * src
   */
  src: PropTypes.string.isRequired,
  /**
   * заголовок tooltip
   */
  label: PropTypes.string.isRequired,
  /**
   * массив для списка tooltip
   */
  fieldKey: PropTypes.array.isRequired,
  /**
   * trigger показывать tooltip по hover или click
   */
  trigger: PropTypes.string.isRequired,
  /**
   * расположение tooltip
   */
  placement: PropTypes.string.isRequired,
  /**
   * применить к label dashed underline
   */
  labelDashed: PropTypes.bool.isRequired,
};

ListTextCell.defaultProps = {
  placement: 'bottom',
  trigger: 'hover',
  fieldKey: [],
  labelDashed: false,
};
