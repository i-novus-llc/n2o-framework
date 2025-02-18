import React from 'react'
import classNames from 'classnames'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { useResolved } from '../../../../../core/Expression/useResolver'
import { FieldsetProps, FieldsetModel } from '../types'

import { MultiFieldsetItemToolbar } from './MultiFieldsetItemToolbar'

interface Props {
    canRemoveFirstItem: boolean
    disabled?: boolean
    enabled: boolean
    index: number
    label?: string
    model: FieldsetModel
    needCopyButton?: boolean
    needRemoveButton?: boolean
    onCopyField(index: number): void
    onRemoveField(index: number): void
    parentName: string
    render: FieldsetProps['render']
    rows: FieldsetProps['rows']
}

export function MultiFieldsetItem({
    disabled: propsDisabled,
    enabled,
    index,
    label: propsLabel,
    model,
    onCopyField,
    onRemoveField,
    parentName,
    // eslint-disable-next-line react/jsx-no-useless-fragment
    render = () => <></>,
    rows = [],
    needRemoveButton: needToRemoveButtonExpression = true,
    canRemoveFirstItem = false,
    needCopyButton: needToCopyButtonExpression = false,
}: Props) {
    const disabled = propsDisabled || !enabled
    const label = useResolved(propsLabel, model)

    const isNeedToCopyButton = useResolved(needToCopyButtonExpression, model)
    const isNeedToRemoveButton = useResolved(needToRemoveButtonExpression, model)

    return (
        <div className="n2o-multi-fieldset__container">
            <div className="n2o-multi-fieldset__item">
                <section className="n2o-multi-fieldset__item-top-section">
                    <div className={classNames('n2o-multi-fieldset__label', { empty: !label })}>
                        <Text>{label}</Text>
                    </div>
                    <MultiFieldsetItemToolbar
                        needCopyButton={isNeedToCopyButton}
                        needRemoveButton={isNeedToRemoveButton}
                        disabled={!enabled}
                        index={index}
                        canRemoveFirstItem={canRemoveFirstItem}
                        onRemoveField={onRemoveField}
                        onCopyField={onCopyField}
                    />
                </section>
                {render(rows, { parentName: `${parentName}[${index}]`, multiSetDisabled: disabled })}
            </div>
        </div>
    )
}
