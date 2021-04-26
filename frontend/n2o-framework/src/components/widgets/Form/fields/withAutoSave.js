import React from 'react'
import PropTypes from 'prop-types'
import { getFormValues } from 'redux-form'
import { compose, withHandlers, mapProps, getContext } from 'recompose'
import has from 'lodash/has'

import { startInvoke } from '../../../../actions/actionImpl'

export default function withAutoSave(WrappedComponent) {
    let timeoutId = null

    function hocComponent(props) {
        return <WrappedComponent {...props} />
    }

    hocComponent.propTypes = {}

    const enhance = compose(
        getContext({
            store: PropTypes.object,
        }),
        withHandlers({
            parseValue: () => (eventOrValue) => {
                if (has(eventOrValue, 'target')) {
                    return eventOrValue.target.value
                }

                return eventOrValue
            },
            prepareData: ({ autoSubmit, store }) => (form, value) => {
                if (autoSubmit) {
                    const model = getFormValues(form)(store.getState())

                    return {
                        ...model,
                    }
                }

                return {
                    data: value,
                }
            },
        }),
        withHandlers({
            onChange: ({
                parseValue,
                prepareData,
                input,
                store,
                autoSubmit,
                dataProvider,
                meta = {},
            }) => (eventOrValue) => {
                const value = parseValue(eventOrValue)
                input.onChange(eventOrValue)

                clearTimeout(timeoutId)

                timeoutId = setTimeout(() => {
                    const { form } = meta
                    const data = prepareData(form, value)

                    store.dispatch(
                        startInvoke(form, autoSubmit || dataProvider, data, null, {}, false),
                    )
                }, 400)
            },
            onBlur: ({ input }) => eventOrValue => input.onBlur(eventOrValue),
        }),
        mapProps(({ input, onChange, onBlur, ...rest }) => ({

            input: { ...input, onChange, onBlur },
            ...rest,
        })),
    )

    return enhance(hocComponent)
}
