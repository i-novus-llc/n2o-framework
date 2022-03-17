import React from 'react'
import PropTypes from 'prop-types'
import { compose, withHandlers, pure, mapProps } from 'recompose'
import { Col } from 'reactstrap'
import get from 'lodash/get'

import evalExpression, { parseExpression } from '../../../utils/evalExpression'

import ReduxField from './ReduxField'
// eslint-disable-next-line import/no-cycle
import FieldsetContainer from './Fieldset'

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
    parentIndex,
    colVisible,
    disabled,
    autoSubmit,
}) {
    if (!colVisible) { return null }

    return (
        <Col xs={col.size || defaultCol} key={colId} className={col.className}>
            {col.fields &&
        col.fields.map((field, i) => {
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
                    parentIndex={parentIndex}
                    disabled={disabled}
                    autoSubmit={autoSubmit}
                    {...field}
                />
            )
        })}
            {col.fieldsets &&
        col.fieldsets.map((fieldset, i) => {
            const key = `set${i}`

            return (
                <FieldsetContainer
                    modelPrefix={modelPrefix}
                    key={key}
                    form={form}
                    parentName={parentName}
                    parentIndex={parentIndex}
                    disabled={disabled}
                    autoSubmit={autoSubmit}
                    activeModel={activeModel}
                    {...fieldset}
                />
            )
        })}
        </Col>
    )
}

const enhance = compose(
    pure,
    withHandlers({
        resolveVisible: props => () => {
            const visible = get(props, 'col.visible')
            const expression = parseExpression(visible)

            if (expression) {
                return evalExpression(expression, props.activeModel)
            } if (visible === true) {
                return true
            } if (visible === false) {
                return false
            }

            return true
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
    form: PropTypes.any,
    parentName: PropTypes.string,
    parentIndex: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    colVisible: PropTypes.bool,
    disabled: PropTypes.bool,
    autoSubmit: PropTypes.bool,
}

const FieldsetCol = enhance(FieldsetColComponent)

export { FieldsetColComponent }
export { FieldsetCol }
export default FieldsetCol
