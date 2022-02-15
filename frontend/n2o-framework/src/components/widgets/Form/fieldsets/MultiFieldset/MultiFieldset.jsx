import React, { useMemo, useState } from 'react'
import { compose, lifecycle, withHandlers } from 'recompose'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import { change } from 'redux-form'
import PropTypes from 'prop-types'

import { registerFieldExtra } from '../../../../../ducks/form/store'
import {
    isInitSelector,
    formValueSelector,
} from '../../../../../ducks/form/selectors'
import evalExpression, {
    parseExpression,
} from '../../../../../utils/evalExpression'
import { resolveExpression } from '../../utils'
import propsResolver from '../../../../../utils/propsResolver'
import HelpPopover from '../../fields/StandardField/HelpPopover'

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
})

export const enhance = compose(
    connect(mapStateToProps),
    lifecycle({
        componentDidUpdate() {
            const { dispatch, isInit, form, name } = this.props

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
        onAddField: ({ form, name, dispatch, fields }) => () => {
            const newValue = fields.slice()

            newValue.push({})
            dispatch(change(form, name, newValue))
        },
        onRemoveField: ({ form, name, dispatch, fields }) => index => () => {
            const newValue = fields.slice()

            newValue.splice(index, 1)

            dispatch(change(form, name, newValue))
        },
        onCopyField: ({ form, name, dispatch, fields }) => index => () => {
            const newValue = fields.slice()

            newValue.splice(fields.length, 0, fields[index])

            dispatch(change(form, name, newValue))
        },
        onRemoveAll: ({
            form,
            name,
            dispatch,
            canRemoveFirstItem,
            fields,
        }) => () => {
            const newValue = fields.slice()

            newValue.splice(+!canRemoveFirstItem, fields.length)

            dispatch(change(form, name, newValue))
        },
        resolvePlaceholder: ({ childrenLabel, firstChildrenLabel, activeModel }) => (index) => {
            const context = { index }
            const expression = parseExpression(childrenLabel)

            if (firstChildrenLabel && index === 0) {
                return propsResolver(firstChildrenLabel, activeModel)
            }

            if (expression) {
                return evalExpression(expression, context)
            }

            return childrenLabel
        },
    }),
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
