import React from 'react';
import PropTypes from 'prop-types';
import { compose, mapProps } from 'recompose';
import map from 'lodash/map';
import { Manager, Reference, Popper } from 'react-popper';
import onClickOutside from 'react-onclickoutside';
import cn from 'classnames';

import DropdownItem from 'reactstrap/lib/DropdownItem';

import Factory from '../../../core/factory/Factory';
import { BUTTONS } from '../../../core/factory/factoryLevels';
import SimpleButton from '../Simple/Simple';
import mappingProps from '../Simple/mappingProps';
import withActionButton from '../withActionButton';

class DropdownButton extends React.Component {
  state = { open: false };

  toggle = () => {
    this.setState(state => ({
      open: !state.open,
    }));
  };

  handleClickOutside = () => {
    const { open } = this.state;

    if (open) {
      this.toggle();
    }
  };

  render() {
    const { subMenu, ...rest } = this.props;
    const { open } = this.state;

    return (
      <div className="n2o-dropdown">
        <Manager>
          <Reference>
            {({ ref }) => (
              <SimpleButton
                {...rest}
                onClick={this.toggle}
                innerRef={ref}
                className="n2o-dropdown-control dropdown-toggle"
                caret
              />
            )}
          </Reference>
          <Popper
            placement="bottom-start"
            modifiers={{
              preventOverflow: {
                boundariesElement: 'window',
              },
            }}
            positionFixed={true}
          >
            {({ ref, style, placement }) => (
              <div
                ref={ref}
                style={style}
                data-placement={placement}
                className={cn('dropdown-menu n2o-dropdown-menu', {
                  'd-block': open,
                })}
              >
                {map(subMenu, ({ src, component, ...rest }) => {
                  return component ? (
                    React.createElement(component, rest)
                  ) : (
                    <Factory
                      key={rest.id}
                      {...rest}
                      level={BUTTONS}
                      src={src}
                      tag={DropdownItem}
                    />
                  );
                })}
              </div>
            )}
          </Popper>
        </Manager>
      </div>
    );
  }
}

DropdownButton.propTypes = {
  subMenu: PropTypes.array,
};

DropdownButton.defaultProps = {
  subMenu: [],
};

export default compose(
  withActionButton(),
  mapProps(props => ({
    ...mappingProps(props),
    subMenu: props.subMenu,
  })),
  onClickOutside
)(DropdownButton);
