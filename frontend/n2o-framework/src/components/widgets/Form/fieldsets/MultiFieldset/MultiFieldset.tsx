import React, { useCallback } from 'react'
import { Button } from 'reactstrap'
import { isEmpty, isNil } from 'lodash'

import HelpPopover from '../../fields/StandardField/HelpPopover'
import { withFieldsetHeader } from '../withFieldsetHeader'
import { useFieldArray } from '../../../../core/FormProvider'
import { useResolved } from '../../../../../core/Expression/useResolver'
import { ArrayFieldProvider } from '../../../../../core/datasource/ArrayField/ArrayFieldProvider'
import { RowProvider } from '../../../../../core/datasource/ArrayField/RowProvider'
import { FieldsetProps } from '../types'

import { MultiFieldsetItem } from './MultiFieldsetItem'

export type Props = Pick<FieldsetProps,
    'activeModel' | 'addButtonLabel' | 'canRemoveFirstItem' | 'childrenLabel' | 'enabled' |
    'firstChildrenLabel' | 'generatePrimaryKey' | 'help' | 'label' | 'name' |
    'needAddButton' | 'needRemoveAllButton' | 'primaryKey' | 'removeAllButtonLabel' | 'rows' | 'render'
>
function MultiFieldset({
    childrenLabel,
    enabled: enabledExpression,
    firstChildrenLabel,
    generatePrimaryKey,
    help,
    label,
    name = '',
    activeModel = {},
    primaryKey = 'id',
    removeAllButtonLabel: removeAllButtonsLabelExpression = 'Удалить все',
    addButtonLabel: addButtonLabelExpression = 'Добавить',
    canRemoveFirstItem: canRemoveFirstItemExpression = false,
    needAddButton: needToAddButtonExpression = true,
    needRemoveAllButton: needToRemoveAllButtonsExpression = false,
    ...props
}: Props) {
    const isEnabled = useResolved(isNil(enabledExpression) ? true : enabledExpression, activeModel)

    const labelAddButton = useResolved(addButtonLabelExpression, activeModel)
    const labelRemoveAllButtons = useResolved(removeAllButtonsLabelExpression, activeModel)

    const isFirstItemCanBeRemoved = useResolved(canRemoveFirstItemExpression, activeModel)
    const isNeedToAddButton = useResolved(needToAddButtonExpression, activeModel)
    const isNeedToRemoveAllButtons = useResolved(needToRemoveAllButtonsExpression, activeModel)

    const { fields, append, remove, copy } = useFieldArray({ name, primaryKey: generatePrimaryKey ? primaryKey : undefined })

    const onRemoveAll = useCallback(() => {
        remove(isFirstItemCanBeRemoved ? 0 : 1, fields.length)
    }, [isFirstItemCanBeRemoved, fields.length, remove])

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
                            onRemoveField={remove}
                            onCopyField={copy}
                            parentName={name}
                            enabled={isEnabled}
                            canRemoveFirstItem={isFirstItemCanBeRemoved}
                        />
                    </RowProvider>
                ))}
                <div className="n2o-multi-fieldset__actions n2o-multi-fieldset__actions--common">
                    {isNeedToAddButton && (
                        <Button
                            className="n2o-multi-fieldset__add"
                            onClick={append}
                            disabled={!isEnabled}
                        >
                            <i className="fa fa-plus mr-1" />
                            {labelAddButton}
                        </Button>
                    )}
                    {!isEmpty(fields) && isNeedToRemoveAllButtons && (
                        <Button
                            className="n2o-multi-fieldset__remove-all"
                            onClick={onRemoveAll}
                            disabled={!isEnabled}
                        >
                            <i className="fa fa-trash mr-1" />
                            {labelRemoveAllButtons}
                        </Button>
                    )}
                </div>
            </div>
        </ArrayFieldProvider>
    )
}

export default withFieldsetHeader(MultiFieldset)
