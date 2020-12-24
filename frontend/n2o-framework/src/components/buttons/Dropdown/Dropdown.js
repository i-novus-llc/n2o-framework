import React from 'react';
import PropTypes from 'prop-types';
import { compose, mapProps } from 'recompose';
import map from 'lodash/map';
import get from 'lodash/get';
import values from 'lodash/values';
import some from 'lodash/some';
import { Manager, Reference, Popper } from 'react-popper';
import onClickOutside from 'react-onclickoutside';
import classNames from 'classnames';

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
    const {
      subMenu,
      id: entityKey,
      className,
      showToggleIcon,
      toolbar,
      ...rest
    } = this.props;
    const { open } = this.state;

    const subMenuMeta = get(toolbar, this.props.id);
    const dropdownVisible = some(values(subMenuMeta), meta => meta.visible);
    const subMenuBody = map(subMenu, ({ src, component, ...rest }) => {
      return component ? (
        React.createElement(component, Object.assign({}, rest, { entityKey }))
      ) : (
        <Factory
          key={rest.id}
          {...rest}
          entityKey={entityKey}
          level={BUTTONS}
          src={src}
          className={classNames('dropdown-item', rest.className)}
          tag="div"
        />
      );
    });

    if (!dropdownVisible) {
      return (
        <div className="n2o-dropdown" style={{ dispaly: 'none' }}>
          {/* Нужно рендерить сабменю, иначе оно не появится в цикле обработки и не посчитается видимость */}
          {subMenuBody}
        </div>
      );
    }

    return (
      <div className={classNames('n2o-dropdown', { visible: dropdownVisible })}>
        <Manager>
          <Reference>
            {({ ref }) => (
              <SimpleButton
                {...rest}
                onClick={this.toggle}
                innerRef={ref}
                className={classNames('n2o-dropdown-control', {
                  className: className,
                  'dropdown-toggle': showToggleIcon,
                })}
                caret
              />
            )}
          </Reference>
          <Popper placement="bottom-start" strategy="fixed">
            {({ ref, style, placement }) => (
              <div
                ref={ref}
                style={style}
                data-placement={placement}
                className={classNames('dropdown-menu n2o-dropdown-menu', {
                  'd-block': open,
                })}
              >
                {subMenuBody}
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
  showToggleIcon: PropTypes.bool,
};

DropdownButton.defaultProps = {
  subMenu: [],
  showToggleIcon: true,
};

export default compose(
  withActionButton(),
  mapProps(props => ({
    ...mappingProps(props),
    subMenu: props.subMenu,
  })),
  onClickOutside
)(DropdownButton);
