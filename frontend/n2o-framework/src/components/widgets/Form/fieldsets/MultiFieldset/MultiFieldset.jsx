import React, { useCallback } from 'react'
import PropTypes from 'prop-types'
import { Button } from 'reactstrap'
import { isEmpty, isNil } from 'lodash'

import HelpPopover from '../../fields/StandardField/HelpPopover'
import { withFieldsetHeader } from '../withFieldsetHeader'
import { useFieldArray } from '../../../../core/FormProvider'
import { useResolved } from '../../../../../core/Expression/useResolver'
import { ArrayFieldProvider } from '../../../../../core/datasource/ArrayField/ArrayFieldProvider'
import { RowProvider } from '../../../../../core/datasource/ArrayField/RowProvider'

import { MultiFieldsetItem } from './MultiFieldsetItem'

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
    needAddButton,
    addButtonLabel,
    removeAllButtonLabel,
    needRemoveAllButton,
    ...props
}) {
    const isEnabled = useResolved(
        isNil(enabledExpression) ? true : enabledExpression,
        activeModel,
    )
    const { fields, append, remove, copy } = useFieldArray({
        name,
        primaryKey: generatePrimaryKey ? primaryKey : undefined,
    })

    const onRemoveAll = useCallback(() => {
        remove(canRemoveFirstItem ? 0 : 1, fields.length)
    }, [canRemoveFirstItem, fields.length, remove])

    return (
        <ArrayFieldProvider>
            <div className="n2o-multi-fieldset">
                {help && !label && <HelpPopover help={help} />}
                {fields.map((field, index) => (
                    <RowProvider index={index}>
                        <MultiFieldsetItem
                            {...props}
                            index={index}
                            model={activeModel}
                            label={firstChildrenLabel && index === 0 ? firstChildrenLabel : childrenLabel}
                            onAddField={append}
                            onRemoveField={remove}
                            onCopyField={copy}
                            onRemoveAll={onRemoveAll}
                            parentName={name}
                            enabled={isEnabled}
                            canRemoveFirstItem={canRemoveFirstItem}
                        />
                    </RowProvider>
                ))}
                <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--common">
                    {needAddButton && (
                        <Button
                            className="n2o-multi-fieldset__add"
                            onClick={append}
                            disabled={!isEnabled}
                        >
                            <i className="fa fa-plus mr-1" />
                            {addButtonLabel}
                        </Button>
                    )}
                    {!isEmpty(fields) && needRemoveAllButton && (
                        <Button
                            className="n2o-multi-fieldset__remove-all"
                            onClick={onRemoveAll}
                            disabled={!isEnabled}
                        >
                            <i className="fa fa-trash mr-1" />
                            {removeAllButtonLabel}
                        </Button>
                    )}
                </div>
            </div>
        </ArrayFieldProvider>
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
    addButtonLabel: PropTypes.string,
    removeAllButtonLabel: PropTypes.string,
    needAddButton: PropTypes.bool,
    needRemoveAllButton: PropTypes.bool,
}

MultiFieldset.defaultProps = {
    addButtonLabel: 'Добавить',
    removeAllButtonLabel: 'Удалить все',
    needAddButton: true,
    needRemoveAllButton: false,
    canRemoveFirstItem: false,
}

export default withFieldsetHeader(MultiFieldset)
