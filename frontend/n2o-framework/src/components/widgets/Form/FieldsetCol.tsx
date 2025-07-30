import React, { useContext, memo, Context, ComponentType } from 'react'
import flowRight from 'lodash/flowRight'
import { Col } from 'reactstrap'
import get from 'lodash/get'

import { ExpressionContext } from '../../../core/Expression/Context'
import { executeExpression } from '../../../core/Expression/execute'
import { parseExpression } from '../../../core/Expression/parse'

import ReduxField from './ReduxField'
import { FieldsetContainer } from './Fieldset'
import { type FieldSetColComponentProps } from './types'

function FieldsetColComponent({
    col,
    activeModel,
    colId,
    autoFocusId,
    labelPosition,
    labelWidth,
    labelAlignment,
    modelPrefix,
    form,
    parentName,
    disabled,
    autoSubmit,
    onChange,
    onBlur,
    multiSetDisabled,
    evalContext,
}: FieldSetColComponentProps) {
    const resolveVisible = () => {
        const visible = get(col, 'visible')
        const expression = parseExpression(visible)

        if (expression) { return executeExpression(expression, activeModel, evalContext) }

        if (visible === true) { return true }

        return visible !== false
    }

    const colVisible = resolveVisible()

    if (!colVisible) { return null }

    const { size, className, style, fields, fieldsets } = col

    return (
        <Col xs={size} key={colId} className={className} style={style}>
            {
                fields?.map((field, i) => {
                    const autoFocus = field.id && autoFocusId && field.id === autoFocusId
                    const key = `field${i}`
                    const name = parentName ? `${parentName}.${field.id}` : field.id

                    return (
                        <ReduxField
                            labelPosition={labelPosition}
                            labelWidth={labelWidth}
                            labelAlignment={labelAlignment}
                            key={key}
                            autoFocus={autoFocus}
                            form={form}
                            modelPrefix={modelPrefix}
                            name={name}
                            parentName={parentName}
                            disabled={disabled}
                            autoSubmit={autoSubmit}
                            onChange={onChange}
                            onBlur={onBlur}
                            multiSetDisabled={multiSetDisabled}
                            {...field}
                        />
                    )
                })}
            {
                fieldsets?.map((fieldset, i) => {
                    const { name: fieldsetName, ...rest } = fieldset

                    const key = `set${i}`
                    const name = parentName ? `${parentName}.${fieldsetName}` : fieldsetName

                    return (
                        <FieldsetContainer
                            modelPrefix={modelPrefix}
                            key={key}
                            name={name}
                            form={form}
                            parentName={parentName}
                            disabled={disabled}
                            autoSubmit={autoSubmit}
                            activeModel={activeModel}
                            onChange={onChange}
                            onBlur={onBlur}
                            multiSetDisabled={multiSetDisabled}
                            {...rest}
                        />
                    )
                })}
        </Col>
    )
}

const withContext = (
    Context: Context<Record<string, unknown>>,
    mapper: (contextValue: Record<string, unknown>) => Record<string, unknown>,
) => (Component: ComponentType<FieldSetColComponentProps>) => (props: FieldSetColComponentProps) => {
    const contextValue = useContext(Context)
    const mappedValue = mapper(contextValue)

    return (<Component {...props} {...mappedValue} />)
}

const enhance = flowRight(
    memo,
    withContext(ExpressionContext, evalContext => ({ evalContext })),
)

const FieldsetCol = enhance(FieldsetColComponent)

export { FieldsetColComponent }
export { FieldsetCol }
export default FieldsetCol
