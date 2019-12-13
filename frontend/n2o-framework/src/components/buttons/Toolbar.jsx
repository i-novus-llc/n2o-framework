import React from 'react';
import PropTypes from 'prop-types';
import { map } from 'lodash';
import { ButtonToolbar, ButtonGroup } from 'reactstrap';

import Factory from '../../core/factory/Factory';
import { BUTTONS } from "../../core/factory/factoryLevels";
import cn from 'classnames';

function Toolbar({ className, toolbar, entityKey }) {
  const renderButtons = props => <Factory {...props} level={BUTTONS} src={'PerformButton'} entityKey={entityKey} />;

  const renderBtnGroup = ({ buttons }) => <ButtonGroup>{map(buttons, renderButtons)}</ButtonGroup>;

  return <ButtonToolbar className={cn('buttons-toolbar', className)}>{map(toolbar, renderBtnGroup)}</ButtonToolbar>;

}

Toolbar.propTypes = {
  toolbar: PropTypes.array,
  entityKey: PropTypes.string
};

Toolbar.defaultProps = {
  toolbar: []
};

export default Toolbar;
