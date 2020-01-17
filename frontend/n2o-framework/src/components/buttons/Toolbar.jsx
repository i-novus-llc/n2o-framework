import React from 'react';
import PropTypes from 'prop-types';
import map from 'lodash/map';
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

  const renderButtons = props =>
    props.component ? (
      React.createElement(props.component, props)
    ) : (
      <Factory level={BUTTONS} entityKey={entityKey} {...props} />
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
