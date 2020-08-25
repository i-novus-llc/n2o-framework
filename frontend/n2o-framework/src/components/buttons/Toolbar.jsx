import React from 'react';
import PropTypes from 'prop-types';
import map from 'lodash/map';
import get from 'lodash/get';
import isUndefined from 'lodash/isUndefined';
import omit from 'lodash/omit';
import ButtonToolbar from 'reactstrap/lib/ButtonToolbar';
import ButtonGroup from 'reactstrap/lib/ButtonGroup';

import Factory from '../../core/factory/Factory';
import { BUTTONS } from '../../core/factory/factoryLevels';
import cn from 'classnames';

function Toolbar({ className, toolbar, entityKey, onClick }) {
  const handleClick = e => {
    e.stopPropagation();
    onClick();
  };

  const remapButtons = props => {
    const subMenu = get(props, 'subMenu');
    const enabled = item => get(item, 'enabled');
    return subMenu
      ? {
          ...props,
          entityKey,
          subMenu: map(subMenu, item => {
            return !isUndefined(enabled(item))
              ? {
                  ...omit(item, ['enabled']),
                  disabled: !item.enabled,
                }
              : item;
          }),
        }
      : !isUndefined(enabled(props))
      ? {
          ...omit(props, ['enabled']),
          entityKey,
          disabled: !enabled(props),
        }
      : props;
  };

  const renderButtons = props => {
    return props.component ? (
      React.createElement(props.component, remapButtons(props))
    ) : (
      <Factory level={BUTTONS} {...props} entityKey={entityKey} />
    );
  };

  const renderBtnGroup = ({ buttons }) => (
    <ButtonGroup>{map(buttons, renderButtons)}</ButtonGroup>
  );

  return (
    <ButtonToolbar
      className={cn('buttons-toolbar', className)}
      onClick={handleClick}
    >
      {map(toolbar, renderBtnGroup)}
    </ButtonToolbar>
  );
}

Toolbar.propTypes = {
  toolbar: PropTypes.array,
  entityKey: PropTypes.string,
  onClick: PropTypes.func,
};

Toolbar.defaultProps = {
  toolbar: [],
  onClick: () => {},
};

export default Toolbar;
