import React from 'react';
import PropTypes from 'prop-types';
import map from 'lodash/map';
import isUndefined from 'lodash/isUndefined';
import setWith from 'lodash/setWith';
import unset from 'lodash/unset';
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

  const remapButtons = obj => {
    if (!isUndefined(obj.enabled)) {
      setWith(obj, 'disabled', !obj.enabled, Object);
      setWith(obj, 'entityKey', entityKey, Object);

      unset(obj, 'enabled');
    }
    if (!isUndefined(obj.subMenu)) {
      map(obj.subMenu, item => remapButtons(item));
    }
    return obj;
  };

  const renderButtons = props =>
    props.component ? (
      React.createElement(props.component, remapButtons(props))
    ) : (
      <Factory level={BUTTONS} {...props} entityKey={entityKey} />
    );

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
