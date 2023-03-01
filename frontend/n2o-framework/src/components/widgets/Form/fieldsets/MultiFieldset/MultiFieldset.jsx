import React, { useCallback, useMemo } from 'react'
import { compose } from 'recompose'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import PropTypes from 'prop-types'

import { dataSourceErrors } from '../../../../../ducks/datasource/selectors'
import evalExpression, {
    parseExpression,
} from '../../../../../utils/evalExpression'
import { resolveExpression } from '../../utils'
import propsResolver from '../../../../../utils/propsResolver'
import HelpPopover from '../../fields/StandardField/HelpPopover'
import { withFieldsetHeader } from '../withFieldsetHeader'
import { isInitSelector } from '../../../../../ducks/form/selectors'
import { useFieldArray } from '../../../../core/FormProvider'

import MultiFieldsetItem from './MultiFieldsetItem'

function MultiFieldset({
    name,
    enabled: enabledExpression,
    activeModel,
    label,
    help,
    canRemoveFirstItem = false,
    generatePrimaryKey,
    primaryKey = 'id',
    firstChildrenLabel,
    childrenLabel,
    form,
    isInit,
    ...props
}) {
    const isEnabled = useMemo(() => resolveExpression(enabledExpression, activeModel), [activeModel, enabledExpression])
    const { fields, append, remove, copy } = useFieldArray({
        name,
        primaryKey: generatePrimaryKey ? primaryKey : undefined,
    })

    const onRemoveAll = useCallback(() => {
        remove(canRemoveFirstItem ? 0 : 1, fields.length)
    }, [canRemoveFirstItem, fields.length, remove])

    const resolvePlaceholder = useCallback((index) => {
        const context = { ...activeModel, index }
        const expression = parseExpression(childrenLabel)

        if (firstChildrenLabel && index === 0) {
            return propsResolver(firstChildrenLabel, context)
        }

        if (expression) {
            return evalExpression(expression, context)
        }

        return childrenLabel
    }, [activeModel, childrenLabel, firstChildrenLabel])

    return (
        <div className="n2o-multi-fieldset">
            {help && !label && <HelpPopover help={help} />}
            <MultiFieldsetItem
                {...props}
                resolvePlaceholder={resolvePlaceholder}
                onAddField={append}
                onRemoveField={remove}
                onCopyField={copy}
                onRemoveAll={onRemoveAll}
                fields={fields}
                parentName={name}
                enabled={isEnabled}
                canRemoveFirstItem={canRemoveFirstItem}
            />
        </div>
    )
}

MultiFieldset.propTypes = {
    name: PropTypes.string,
    enabled: PropTypes.string,
    visible: PropTypes.string,
    activeModel: PropTypes.object,
    label: PropTypes.string,
    help: PropTypes.string,
    canRemoveFirstItem: PropTypes.bool,
    generatePrimaryKey: PropTypes.bool,
    primaryKey: PropTypes.bool,
    firstChildrenLabel: PropTypes.string,
    childrenLabel: PropTypes.string,
    form: PropTypes.string,
    isInit: PropTypes.bool,
}

const mapStateToProps = createStructuredSelector({
    isInit: (state, props) => isInitSelector(props.form, props.name)(state),
    errors: (state, props) => dataSourceErrors(props.form, props.modelPrefix)(state),
})

export const enhance = compose(
    connect(mapStateToProps),
    withFieldsetHeader,
)

export default enhance(MultiFieldset)
