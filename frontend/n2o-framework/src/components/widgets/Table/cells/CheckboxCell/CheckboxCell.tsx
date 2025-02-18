import React, { useState, useEffect, useMemo, ChangeEvent, MouseEvent } from 'react'
import get from 'lodash/get'
import set from 'lodash/set'
import flowRight from 'lodash/flowRight'
import { push } from 'connected-react-router'
import { Checkbox } from '@i-novus/n2o-components/lib/inputs/Checkbox/Checkbox'

import { WithCell } from '../../withCell'
import withTooltip from '../../withTooltip'

import { type Props } from './types'

function CheckboxCellBody({
    visible = true,
    disabled = false,
    model,
    fieldKey,
    id,
    callAction,
    url,
    target,
    dispatch,
    ...rest
}: Props) {
    const checkedFromData = useMemo(() => get(model, fieldKey || id), [fieldKey, id, model])
    const [checked, setChecked] = useState(checkedFromData)

    useEffect(() => {
        setChecked(checkedFromData)
    }, [checkedFromData])

    if (!visible) { return null }

    const handleClick = (e: MouseEvent) => { e.stopPropagation() }
    const handleChange = (e: ChangeEvent<HTMLInputElement>) => {
        const { checked } = e.nativeEvent.target as HTMLInputElement

        // TODO сделать единый redirect func для кнопок, ячеек, linkImpl, meta saga итд.
        //  разобраться с синтаксисом, прим. где-то _self, а где-то self
        const direct = () => {
            if (!url) { return null }

            if (target === 'application') {
                dispatch(push(url))

                return null
            }

            if (target === '_blank') {
                window.open(url)

                return null
            }

            window.location.href = url

            return null
        }

        const data = set({ ...model }, fieldKey || id, checked)

        setChecked(checked)
        callAction(data)
        direct()
    }

    return (
        <Checkbox
            className="сheckbox-сell"
            inline
            onClick={handleClick}
            onChange={handleChange}
            disabled={disabled}
            checked={checked}
            {...rest}
        />
    )
}

const CheckboxCell = flowRight(WithCell, withTooltip)(CheckboxCellBody)

CheckboxCell.displayName = 'CheckboxCell'

export { CheckboxCell }
export default CheckboxCell
