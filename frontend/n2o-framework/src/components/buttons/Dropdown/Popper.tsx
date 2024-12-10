import classNames from 'classnames'
import { Manager, Popper as PopperComponent, Reference } from 'react-popper'
import React from 'react'
import onClickOutsideHOC from 'react-onclickoutside'

import { SimpleButton } from '../Simple/Simple'

import { SubMenu } from './SubMenu'
import { DropdownComponentProps } from './types'

function Component({
    visible,
    actionCallback,
    toggle,
    onClick,
    subMenu,
    entityKey,
    className,
    tooltipTriggerRef,
    showToggleIcon,
    popperKey,
    open,
    forwardedRef,
    ...rest
}: DropdownComponentProps) {
    return (
        <div className={classNames('n2o-dropdown', { visible })} ref={forwardedRef}>
            <Manager>
                <Reference>
                    {({ ref }) => (
                        <SimpleButton
                            {...rest}
                            forwardedRef={forwardedRef}
                            // FIXME из за автотеста который для чего то проверяет что кнопки в DOM но невидимы
                            // visible={visible}
                            actionCallback={actionCallback}
                            onClick={toggle}
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
                <PopperComponent key={popperKey} placement="bottom-start" strategy="fixed">
                    {({ ref, style, placement }) => (
                        <div
                            ref={ref}
                            style={style}
                            data-placement={placement}
                            className={classNames('dropdown-menu n2o-dropdown-menu', { 'd-block': open })}
                        >
                            <SubMenu
                                items={subMenu}
                                actionCallback={actionCallback}
                                entityKey={entityKey}
                                toggle={toggle}
                                onClick={onClick}
                                {...rest}
                            />
                        </div>
                    )}
                </PopperComponent>
            </Manager>
        </div>
    )
}

export const Popper = onClickOutsideHOC(Component)
