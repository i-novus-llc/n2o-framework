import React from 'react'
import classNames from 'classnames'

import { Factory } from '../../../core/factory/Factory'
import { BUTTONS } from '../../../core/factory/factoryLevels'

import { type SubMenuProps } from './types'

export function SubMenu({ items, actionCallback, entityKey, toggle, onClick }: SubMenuProps) {
    return (
        // eslint-disable-next-line react/jsx-no-useless-fragment
        <>
            {items?.map(({ src, component, ...btn }) => {
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
            })}
        </>
    )
}
