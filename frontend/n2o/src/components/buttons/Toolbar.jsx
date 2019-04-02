import React from 'react';
import PropTypes from 'prop-types';
import { map } from 'lodash';
import { ButtonToolbar, ButtonGroup } from 'reactstrap';

import Factory from '../../core/factory/Factory';

function Toolbar({ toolbar, entityKey }) {
  const renderButtons = props => <Factory {...props} entityKey={entityKey} />;

  const renderBtnGroup = ({ buttons }) => <ButtonGroup>{map(buttons, renderButtons)}</ButtonGroup>;

  return <ButtonToolbar>{map(toolbar, renderBtnGroup)}</ButtonToolbar>;
}

Toolbar.propTypes = {
  toolbar: PropTypes.array,
  entityKey: PropTypes.string
};

Toolbar.defaultProps = {
  toolbar: []
};

export default Toolbar;
