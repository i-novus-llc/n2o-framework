import React, { useContext } from 'react'
import PropTypes from 'prop-types'
import { compose, withHandlers, pure, mapProps } from 'recompose'
import { Col } from 'reactstrap'
import get from 'lodash/get'

import { SecurityController } from '../../../core/auth/SecurityController'
import { ExpressionContext } from '../../../core/Expression/Context'
import { executeExpression } from '../../../core/Expression/execute'
import { parseExpression } from '../../../core/Expression/parse'

import ReduxField from './ReduxField'
// eslint-disable-next-line import/no-cycle
import { FieldsetContainer } from './Fieldset'

function FieldsetColComponent({
    col,
    activeModel,
    defaultCol,
    colId,
    autoFocusId,
    labelPosition,
    labelWidth,
    labelAlignment,
    modelPrefix,
    form,
    parentName,
    colVisible,
    disabled,
    autoSubmit,
    onChange,
    onBlur,
    multiSetDisabled,
}) {
    if (!colVisible) { return null }

    const { security, size, className, style, fields, fieldsets } = col

    return (
        <SecurityController config={security}>
            <Col xs={size || defaultCol} key={colId} className={className} style={style}>
                {fields &&
                    fields.map((field, i) => {
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
                {fieldsets &&
                    fieldsets.map((fieldset, i) => {
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
        </SecurityController>
    )
}

const withContext = (Context, mapper) => Component => (props) => {
    const contextValue = useContext(Context)
    const mappedValue = mapper(contextValue)

    return (<Component {...props} {...mappedValue} />)
}

const enhance = compose(
    pure,
    withContext(ExpressionContext, evalContext => ({ evalContext })),
    withHandlers({
        resolveVisible: props => () => {
            const visible = get(props, 'col.visible')
            const expression = parseExpression(visible)

            if (expression) {
                return executeExpression(expression, props.activeModel, props.evalContext)
            } if (visible === true) {
                return true
            }

            return visible !== false
        },
    }),

    mapProps(({ resolveVisible, ...props }) => ({
        ...props,
        colVisible: resolveVisible(),
    })),
)

FieldsetColComponent.propTypes = {
    col: PropTypes.object,
    activeModel: PropTypes.object,
    defaultCol: PropTypes.object,
    colId: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    autoFocusId: PropTypes.oneOfType([PropTypes.string, PropTypes.oneOf([false])]),
    labelPosition: PropTypes.string,
    labelWidth: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    labelAlignment: PropTypes.string,
    modelPrefix: PropTypes.string,
    form: PropTypes.string,
    parentName: PropTypes.string,
    colVisible: PropTypes.bool,
    disabled: PropTypes.bool,
    autoSubmit: PropTypes.bool,
    onChange: PropTypes.func,
    onBlur: PropTypes.func,
}

const FieldsetCol = enhance(FieldsetColComponent)

export { FieldsetColComponent }
export { FieldsetCol }
export default FieldsetCol
