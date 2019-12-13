import React from 'react';
import PropTypes from 'prop-types';
import { compose, mapProps } from 'recompose';
import { map } from 'lodash';
import { ButtonDropdown, DropdownToggle, DropdownMenu, DropdownItem } from 'reactstrap';

import Factory from '../../../core/factory/Factory';
import { BUTTONS } from '../../../core/factory/factoryLevels';
import SimpleButton from '../Simple/Simple';
import mappingProps from '../Simple/mappingProps';
import withActionButton from '../withActionButton';

class DropdownButton extends React.Component {
  state = { open: false };

  toggle = () => {
    this.setState(state => ({
      open: !state.open
    }));
  };

  render() {
    const { subMenu, ...rest } = this.props;
    const { open } = this.state;

    return (
      <ButtonDropdown isOpen={open} toggle={this.toggle}>
        <SimpleButton {...rest} tag={DropdownToggle} caret />
        <DropdownMenu persist>
          {map(subMenu, ({ src, ...rest }) => {
            return <Factory key={rest.id} {...rest} level={BUTTONS} src={src} tag={DropdownItem} />;
          })}
        </DropdownMenu>
      </ButtonDropdown>
    );
  }
}

DropdownButton.propTypes = {
  subMenu: PropTypes.array
};

DropdownButton.defaultProps = {
  subMenu: []
};

export default compose(
  withActionButton(),
  mapProps(props => ({
    ...mappingProps(props),
    subMenu: props.subMenu
  }))
)(DropdownButton);
