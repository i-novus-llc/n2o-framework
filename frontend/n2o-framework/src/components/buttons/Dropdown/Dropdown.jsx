import React from 'react'
import { connect } from 'react-redux'
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
import { SimpleButton } from '../Simple/Simple'
import mappingProps from '../Simple/mappingProps'
import withActionButton from '../withActionButton'

function SubMenu(props) {
    const { items, actionCallback, entityKey, toggle, onClick } = props

    return map(items, ({ src, component, ...btn }) => {
        if (component) {
            return React.createElement(component, { ...btn, actionCallback, entityKey, onClick: toggle })
        }

        return (
            <Factory
                key={btn.id}
                {...btn}
                entityKey={entityKey}
                level={BUTTONS}
                src={src}
                onClick={onClick}
                className={classNames('dropdown-item dropdown-item-btn', btn.className)}
                tag="div"
                nested
            />
        )
    })
}

class DropdownButton extends React.Component {
  state = { opened: false, popperKey: 0 }

  toggle = () => {
      let { popperKey } = this.state
      const { opened } = this.state

      if (!opened) { popperKey += 1 }

      this.setState({ opened: !opened, popperKey })
  };

    onClick = () => this.setState({ opened: false })

    handleClickOutside = () => this.onClick()

    render() {
        const {
            subMenu, entityKey, className,
            showToggleIcon, tooltipTriggerRef,
            getStoreButtons, actionCallback, ...rest
        } = this.props

        const { opened, popperKey } = this.state
        const storeButtons = getStoreButtons(entityKey)
        let visible = false

        if (storeButtons) {
            visible = some(subMenu, (item = {}) => {
                const { visible } = item

                if (visible !== undefined) { return visible }

                const { id } = item

                return get(storeButtons, `${id}.visible`)
            })
        }

        return (
            <div className={classNames('n2o-dropdown', { visible })}>
                <Manager>
                    <Reference>
                        {({ ref }) => (
                            <SimpleButton
                                {...rest}
                                actionCallback={actionCallback}
                                onClick={this.toggle}
                                innerRef={ref}
                                tooltipTriggerRef={tooltipTriggerRef}
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
                                className={classNames('dropdown-menu n2o-dropdown-menu', { 'd-block': opened })}
                            >
                                <SubMenu
                                    items={subMenu}
                                    actionCallback={actionCallback}
                                    entityKey={entityKey}
                                    toggle={this.toggle}
                                    onClick={this.onClick}
                                />
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
    subMenu: PropTypes.array,
    showToggleIcon: PropTypes.bool,
    tooltipTriggerRef: PropTypes.func,
}

DropdownButton.defaultProps = { subMenu: [], showToggleIcon: true }

export default compose(
    withActionButton(),
    mapProps(props => ({ ...mappingProps(props), subMenu: props.subMenu })),
    connect(state => ({ getStoreButtons: id => state.toolbar[id] || null })),
    onClickOutside,
)(DropdownButton)
