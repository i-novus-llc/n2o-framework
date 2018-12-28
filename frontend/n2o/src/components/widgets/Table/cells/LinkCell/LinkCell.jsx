import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import { get } from 'lodash';
import { Button } from 'reactstrap';
import withCell from '../../withCell';
import { LinkCellType } from './linkCellTypes';

function LinkCell({ id, fieldKey, className, style, model, callActionImpl, icon, type, ...rest }) {
  return (
    <Button
      color="link"
      style={style}
      className={cn('n2o-link-cell', 'p-0', { [className]: className })}
      onClick={callActionImpl}
    >
      {icon &&
        (type === LinkCellType.ICON || type === LinkCellType.ICONANDTEXT) && (
          <i style={{ marginRight: 5 }} className={icon} />
        )}
      {(type === LinkCellType.ICONANDTEXT || type === LinkCellType.TEXT) &&
        get(model, fieldKey || id)}
    </Button>
  );
}

LinkCell.propTypes = {
  icon: PropTypes.string,
  type: PropTypes.string,
  id: PropTypes.string,
  fieldKey: PropTypes.string,
  model: PropTypes.object
};

LinkCell.defaultProps = {
  type: LinkCellType.TEXT
};

export default withCell(LinkCell);
