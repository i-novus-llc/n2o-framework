import React, { useCallback, useMemo, useRef, useState, type VFC, MouseEvent } from 'react'
import classNames from 'classnames'
import flowRight from 'lodash/flowRight'
import get from 'lodash/get'
import { FormattedText } from '@i-novus/n2o-components/lib/Typography/FormattedText'

import { WithCell } from '../../withCell'
import { withTooltip } from '../../withTooltip'
import { DefaultCell } from '../DefaultCell'

import { View } from './VIew'
import { Control } from './Control'

// eslint-disable-next-line
const Cell: VFC<any> = ({
    visible,
    control,
    editable,
    disabled,
    format,
    fieldKey,
    model,
    callAction,
}) => {
    const controlId = control.id
    const [isEditing, setIsEditing] = useState(false)
    const viewValue = useMemo(() => get(model, fieldKey), [model, fieldKey])
    const controlValue = useMemo(() => get(model, controlId), [model, controlId])
    const modelRef = useRef(model)

    modelRef.current = model

    const isEmpty = !viewValue

    const enableEditing = useCallback((event: MouseEvent<HTMLDivElement>) => {
        event.stopPropagation()
        setIsEditing(true)
    }, [])

    const disableEditing = useCallback(() => {
        setIsEditing(false)
    }, [])

    const handleChange = useCallback((value) => {
        if (model?.[controlId] === value) {
            return
        }

        callAction({ ...model, [controlId]: value })
    }, [callAction, controlId, model])

    if (!visible) {
        return null
    }

    return (
        <DefaultCell
            tag="div"
            disabled={disabled}
            className={classNames({ 'n2o-editable-cell': editable })}
        >
            {(isEditing && editable) ? (
                <Control
                    control={control}
                    value={controlValue}
                    onChange={handleChange}
                    onBlur={disableEditing}
                />
            ) : (
                <View
                    className={classNames({
                        'editable-cell-empty': isEmpty,
                    })}
                    onClick={enableEditing}
                >
                    <FormattedText format={format}>{viewValue}</FormattedText>
                </View>
            )}
        </DefaultCell>
    )
}

Cell.defaultProps = {
    visible: true,
    disabled: false,
    model: {},
    editFieldId: null,
}

Cell.displayName = 'EditableCell'

export const EditableCell = flowRight(
    WithCell,
    withTooltip,
)(Cell)
