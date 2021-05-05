import React from 'react'
import { compose } from 'recompose'
import { Prompt } from 'react-router-dom'
import { reduxForm } from 'redux-form'
import PropTypes from 'prop-types'
import { useTranslation } from 'react-i18next'

import Form from './Form'
import ReduxField from './ReduxField'

/**
 *
 * @reactProps {object} props.prompt - флаг включения обработки выхода с несохраненной формы
 * @reactProps {object} props.dirty - флаг отличия формы от initialState
 * @returns {*}
 * @constructor
 */
function ReduxForm(props) {
    const { t } = useTranslation()

    return (
        <>
            {props.prompt && (
                <Prompt when={props.dirty} message={t('defaultPromptMessage')} />
            )}
            <Form {...props} />
        </>
    )
}

ReduxForm.propTypes = {
    prompt: PropTypes.bool,
}

ReduxForm.defaultProps = {
    prompt: false,
}

ReduxForm.Field = ReduxField

export default compose(
    reduxForm({
        destroyOnUnmount: true,
        enableReinitialize: true,
    }),
)(ReduxForm)
