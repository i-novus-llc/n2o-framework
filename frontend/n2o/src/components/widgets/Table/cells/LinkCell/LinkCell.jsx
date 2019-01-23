import React from 'react';
import PropTypes from 'prop-types';
import cn from 'classnames';
import { get } from 'lodash';
import { Button } from 'reactstrap';
import withCell from '../../withCell';
import { LinkCellType } from './linkCellTypes';

function LinkCell({
  id,
  fieldKey,
  className,
  style,
  model,
  callActionImpl,
  icon,
  type,
  visible,
  ...rest
}) {
  return (
    visible && (
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
    )
  );
}

LinkCell.propTypes = {
  icon: PropTypes.string,
  type: PropTypes.string,
  id: PropTypes.string,
  fieldKey: PropTypes.string,
  model: PropTypes.object.dependencies,
  visible: PropTypes.bool
};

LinkCell.defaultProps = {
  type: LinkCellType.TEXT,
  visible: true
};

export default withCell(LinkCell);
