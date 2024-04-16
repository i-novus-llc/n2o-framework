import React, { useCallback, useMemo, useRef, useState, type VFC } from 'react'
import classNames from 'classnames'
import { compose } from 'recompose'
import get from 'lodash/get'

import { Text } from '../../../../snippets/Text/Text'
import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
import { DefaultCell } from '../DefaultCell'

import { View } from './VIew'
import { Control } from './Control'

// eslint-disable-next-line
const Cell: VFC<any> = (props) => {
    const [isEditing, setIsEditing] = useState(false)
    const {
        id,
        visible,
        control,
        editable,
        disabled,
        format,
        fieldKey,
        editFieldId,
        model,
        callAction,
    } = props
    const field = fieldKey || id
    const viewValue = useMemo(() => get(model, field), [model, field])
    const controlValue = useMemo(() => get(model, editFieldId), [model, editFieldId])
    const modelRef = useRef(model)

    modelRef.current = model

    const isEmpty = !viewValue

    const enableEditing = useCallback(() => {
        setIsEditing(true)
    }, [])

    const disableEditing = useCallback(() => {
        setIsEditing(false)
    }, [])

    const handleChange = useCallback((value) => {
        if (model?.[editFieldId] === value) {
            return
        }

        callAction({ ...model, [editFieldId]: value })
    }, [callAction, editFieldId, model])

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
                    <Text text={viewValue} format={format} />
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

export const EditableCell = compose(
    withCell,
    withTooltip,
)(Cell)
