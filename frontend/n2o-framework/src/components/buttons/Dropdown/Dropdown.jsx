import React from 'react'
import PropTypes from 'prop-types'
import { compose, mapProps } from 'recompose'
import map from 'lodash/map'
import get from 'lodash/get'
import some from 'lodash/some'
import { Manager, Reference, Popper } from 'react-popper'
import onClickOutside from 'react-onclickoutside'
import classNames from 'classnames'

import { Factory } from '../../../core/factory/Factory'
import { BUTTONS } from '../../../core/factory/factoryLevels'
import SimpleButton from '../Simple/Simple'
import mappingProps from '../Simple/mappingProps'
import withActionButton from '../withActionButton'

class DropdownButton extends React.Component {
  state = {
      opened: false,
      popperKey: 0,
  };

  toggle = () => {
      let { popperKey } = this.state
      const { opened } = this.state

      if (!opened) {
          popperKey += 1
      }

      this.setState({ opened: !opened, popperKey })
  };

    onClick = () => {
        this.setState({ opened: false })
    }

    handleClickOutside = () => {
        this.onClick()
    };

    render() {
        const {
            subMenu,
            id: entityKey,
            className,
            showToggleIcon,
            toolbar,
            ...rest
        } = this.props
        const { opened, popperKey } = this.state
        const storesSubMenu = get(toolbar, entityKey)
        let dropdownVisible = false

        if (subMenu && storesSubMenu) {
            dropdownVisible = some(subMenu, (subMenuItem) => {
                if (subMenuItem && subMenuItem.visible !== undefined) {
                    return subMenuItem.visible
                }

                const storesSubMenuItem = storesSubMenu[subMenuItem.id]

                return get(storesSubMenuItem, 'visible')
            })
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
                                    className,
                                    'dropdown-toggle': showToggleIcon,
                                })}
                                caret
                            />
                        )}
                    </Reference>
                    <Popper key={popperKey} placement="bottom-start" strategy="fixed">
                        {({ ref, style, placement }) => (
                            <div
                                ref={ref}
                                style={style}
                                data-placement={placement}
                                className={classNames('dropdown-menu n2o-dropdown-menu', {
                                    'd-block': opened,
                                })}
                            >
                                {map(subMenu, ({ src, component, ...rest }) => (component ? (
                                    React.createElement(
                                        component,
                                        { ...rest, entityKey, onClick: this.toggle },
                                    )
                                ) : (
                                    <Factory
                                        key={rest.id}
                                        {...rest}
                                        entityKey={entityKey}
                                        level={BUTTONS}
                                        src={src}
                                        onClick={this.onClick}
                                        className={classNames('dropdown-item', rest.className)}
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
    id: PropTypes.any,
    className: PropTypes.string,
    toolbar: PropTypes.any,
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
