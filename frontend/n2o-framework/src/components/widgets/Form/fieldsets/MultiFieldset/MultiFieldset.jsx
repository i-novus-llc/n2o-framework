import React, { useMemo, useState } from 'react'
import { compose, lifecycle, withHandlers } from 'recompose'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import { change } from 'redux-form'
import PropTypes from 'prop-types'

import { registerFieldExtra, unregisterMultisetItemExtra } from '../../../../../ducks/form/store'
import {
    isInitSelector,
    formValueSelector,
} from '../../../../../ducks/form/selectors'
import { resetValidationMulti } from '../../../../../ducks/datasource/store'
import { dataSourceErrors } from '../../../../../ducks/datasource/selectors'
import evalExpression, {
    parseExpression,
} from '../../../../../utils/evalExpression'
import { resolveExpression } from '../../utils'
import propsResolver from '../../../../../utils/propsResolver'
import HelpPopover from '../../fields/StandardField/HelpPopover'
import { withFieldsetHeader } from '../withFieldsetHeader'
import { id } from '../../../../../utils/id'

import MultiFieldsetItem from './MultiFieldsetItem'

function MultiFieldset({
    name, enabled, visible, activeModel, label, help,
    ...props
}) {
    const [visibleState, setVisible] = useState(true)
    const [enabledState, setEnabled] = useState(true)

    useMemo(() => {
        setEnabled(resolveExpression(enabled, activeModel))
    }, [enabled, activeModel])
    useMemo(() => {
        setVisible(resolveExpression(visible, activeModel))
    }, [visible, activeModel])

    return (
        <div className="n2o-multi-fieldset">
            {help && !label && <HelpPopover help={help} />}
            <MultiFieldsetItem
                {...props}
                parentName={name}
                visible={visibleState}
                enabled={enabledState}
            />
        </div>
    )
}

const mapStateToProps = createStructuredSelector({
    fields: (state, props) => formValueSelector(props.form, props.name)(state),
    isInit: (state, props) => isInitSelector(props.form, props.name)(state),
    errors: (state, props) => dataSourceErrors(props.form, props.modelPrefix)(state),
})

export const enhance = compose(
    connect(mapStateToProps),
    lifecycle({
        componentDidUpdate(prevProps) {
            const { dispatch, isInit, form, name, fields } = this.props

            if (prevProps.fields.length > fields.length) {
                dispatch(
                    unregisterMultisetItemExtra(form, name, fields.length, true),
                )
            }

            if (!isInit) {
                dispatch(
                    registerFieldExtra(form, name, {
                        type: 'FieldArray',
                    }),
                )
            }
        },
    }),
    withHandlers({
        onAddField: ({
            form,
            name,
            dispatch,
            fields,
            generatePrimaryKey = false,
            primaryKey = 'id',
        }) => () => {
            if (fields === null) {
                const fieldData = generatePrimaryKey ? { [primaryKey]: id() } : {}

                dispatch(change(form, name, [fieldData]))

                return
            }
            const newValue = fields.slice()
            const newField = generatePrimaryKey ? { [primaryKey]: id() } : {}

            newValue.push(newField)
            dispatch(change(form, name, newValue))
        },
        onRemoveField: ({ form, name, dispatch, fields, modelPrefix }) => index => () => {
            const newValue = fields.slice()

            newValue.splice(index, 1)

            dispatch(unregisterMultisetItemExtra(form, name, index))
            dispatch(change(form, name, newValue))
            dispatch(resetValidationMulti(form, name, index, 1, modelPrefix))
        },
        onCopyField: ({
            form,
            name,
            dispatch,
            fields,
            generatePrimaryKey,
            primaryKey,
        }) => index => () => {
            const newValue = fields.slice()
            const copyingRow = { ...fields[index] }

            if (generatePrimaryKey) {
                copyingRow[primaryKey || 'id'] = id()
            }

            newValue.splice(fields.length, 0, copyingRow)

            dispatch(change(form, name, newValue))
        },
        onRemoveAll: ({
            form,
            name,
            dispatch,
            canRemoveFirstItem,
            fields,
            modelPrefix,
        }) => () => {
            const newValue = fields.slice()
            const deleteFrom = canRemoveFirstItem ? 0 : 1

            newValue.splice(deleteFrom, fields.length)

            dispatch(unregisterMultisetItemExtra(form, name, deleteFrom, true))
            dispatch(change(form, name, newValue))
            dispatch(resetValidationMulti(form, name, deleteFrom, fields.length, modelPrefix))
        },
        resolvePlaceholder: ({ childrenLabel, firstChildrenLabel, activeModel }) => (index) => {
            const expression = parseExpression(childrenLabel)
            const fnProps = { ...activeModel, index }

            if (firstChildrenLabel && index === 0) {
                return propsResolver(firstChildrenLabel, fnProps)
            }

            if (expression) {
                return evalExpression(expression, fnProps)
            }

            return childrenLabel
        },
    }),
    withFieldsetHeader,
)

MultiFieldset.propTypes = {
    name: PropTypes.string,
    enabled: PropTypes.bool,
    visible: PropTypes.bool,
    activeModel: PropTypes.object,
    label: PropTypes.string,
    help: PropTypes.string,
}

export default enhance(MultiFieldset)
