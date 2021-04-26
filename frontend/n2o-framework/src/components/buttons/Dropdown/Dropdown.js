import React from 'react'
import PropTypes from 'prop-types'
import { compose, mapProps } from 'recompose'
import map from 'lodash/map'
import { Manager, Reference, Popper } from 'react-popper'
import onClickOutside from 'react-onclickoutside'
import cn from 'classnames'
import DropdownItem from 'reactstrap/lib/DropdownItem'

import Factory from '../../../core/factory/Factory'
import { BUTTONS } from '../../../core/factory/factoryLevels'
import SimpleButton from '../Simple/Simple'
import mappingProps from '../Simple/mappingProps'
import withActionButton from '../withActionButton'

class DropdownButton extends React.Component {
  state = { open: false, initOpen: true };

  toggle = () => {
      this.setState(state => ({
          open: !state.open,
      }))
  };

  handleClickOutside = () => {
      const { open } = this.state

      if (open) {
          this.toggle()
      }
  };

  componentDidMount() {
      // initOpen необходимо для корректной работы адаптивности Popper.
      // Если Popper выходит за пределы viewport или document, но Popper не в DOM -
      // react-popper не перевернет его и он вылетит за пределы окна
      setTimeout(
          () => this.setState({
              // при монтировании скрываем Popper из DOM с минимальной задержкой
              initOpen: false,
          }),
          0,
      )
  }

  render() {
      const {
          subMenu,
          id: entityKey,
          className,
          showToggleIcon,
          ...rest
      } = this.props
      const { open, initOpen } = this.state

      return (
          <div className="n2o-dropdown">
              <Manager>
                  <Reference>
                      {({ ref }) => (
                          <SimpleButton
                              {...rest}
                              onClick={this.toggle}
                              innerRef={ref}
                              className={cn('n2o-dropdown-control', {
                                  className,
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
                              className={cn('dropdown-menu n2o-dropdown-menu', {
                                  'd-block': open,
                                  'dropdown-menu__init-load d-block': initOpen,
                              })}
                          >
                              {map(subMenu, ({ src, component, ...rest }) => (component ? (
                                  React.createElement(
                                      component,
                                      { ...rest, entityKey },
                                  )
                              ) : (
                                  <Factory
                                      key={rest.id}
                                      {...rest}
                                      entityKey={entityKey}
                                      level={BUTTONS}
                                      src={src}
                                      className={cn('dropdown-item', rest.className)}
                                      tag="div"
                                  />
                              )))}
                          </div>
                      )}
                  </Popper>
              </Manager>
          </div>
      )
  }
}

DropdownButton.propTypes = {
    subMenu: PropTypes.array,
    showToggleIcon: PropTypes.bool,
}

DropdownButton.defaultProps = {
    subMenu: [],
    showToggleIcon: true,
}

export default compose(
    withActionButton(),
    mapProps(props => ({
        ...mappingProps(props),
        subMenu: props.subMenu,
    })),
    onClickOutside,
)(DropdownButton)
