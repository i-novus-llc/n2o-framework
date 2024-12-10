import React, { ComponentType, useState } from 'react'
import { connect } from 'react-redux'
import get from 'lodash/get'
import flowRight from 'lodash/flowRight'

import { withActionButton } from '../withActionButton'
import { State } from '../../../ducks/State'

import { type DropdownProps } from './types'
import { Popper } from './Popper'

function DropdownButtonBody({
    entityKey,
    className,
    tooltipTriggerRef,
    actionCallback,
    storeButtons,
    subMenu = [],
    showToggleIcon = true,
    visible: globalVisible = true,
    ...rest
}: DropdownProps) {
    const [open, setOpen] = useState(false)
    const [popperKey, setPopperKey] = useState(0)

    const toggle = () => {
        let newPopperKey = popperKey

        if (!open) { newPopperKey += 1 }

        setOpen(!open)
        setPopperKey(newPopperKey)
    }

    const onClick = () => setOpen(false)

    const visible = storeButtons
        ? subMenu.some((item) => {
            const { visible } = item || {}

            if (visible !== undefined) { return visible }

            const { id } = item

            return get(storeButtons, `${id}.visible`)
        })
        : false

    return (
        <Popper
            {...rest}
            visible={visible && globalVisible}
            actionCallback={actionCallback}
            toggle={toggle}
            onClick={onClick}
            subMenu={subMenu}
            entityKey={entityKey}
            className={className}
            tooltipTriggerRef={tooltipTriggerRef}
            showToggleIcon={showToggleIcon}
            popperKey={popperKey}
            open={open}
            handleClickOutside={onClick}
        />
    )
}

type ExtendedWithHintPosition = ComponentType<DropdownProps & { hintPosition: string }>

function WithHintPosition(Component: ComponentType<DropdownProps>): ExtendedWithHintPosition {
    return function Wrapper(props) {
        const { placement } = props

        return <Component {...props} hintPosition={placement} />
    }
}

export const DropdownButton = flowRight(
    WithHintPosition,
    withActionButton(),
    connect((state: State, props: DropdownProps) => ({
        storeButtons: state?.toolbar[props.entityKey] || null,
    })),
)(DropdownButtonBody)

export default DropdownButton
