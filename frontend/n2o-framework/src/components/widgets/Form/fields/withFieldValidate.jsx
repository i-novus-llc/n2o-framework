import React from 'react'
import { compose, withHandlers, mapProps } from 'recompose'
import every from 'lodash/every'
import has from 'lodash/has'

import * as presets from '../../../../core/validation/presets'
import {
    addFieldMessage,
    removeFieldMessage,
} from '../../../../ducks/form/store'

export default (Field) => {
    function FieldWithValidate(props) {
        return <Field {...props} />
    }

    const enhance = compose(
        withHandlers({
            validateField: ({ dispatch, validation, meta, input, id }) => (value) => {
                let message = {}
                const validateResult = every(
                    validation,
                    ({ severity, text, type, ...options }) => {
                        const validationFunc = presets[type]

                        message = {
                            severity,
                            text,
                        }

                        return validationFunc(id, { [id]: value }, options)
                    },
                )

                if (validateResult) {
                    dispatch(removeFieldMessage(meta.form, input.name))
                } else {
                    dispatch(
                        addFieldMessage(meta.form, input.name, message, meta.touched),
                    )
                }
            },
        }),
        withHandlers({
            onBlur: ({ input, validateField }) => (eventOrValue) => {
                validateField(
                    has(eventOrValue, 'target') ? eventOrValue.target.value : eventOrValue,
                )
                input.onBlur(eventOrValue)
            },
        }),
        mapProps(({ input, onBlur, ...rest }) => ({

            input: { ...input, onBlur },
            ...rest,
        })),
    )

    return enhance(FieldWithValidate)
}
