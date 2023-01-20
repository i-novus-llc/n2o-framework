import React from 'react'
import { Prompt } from 'react-router-dom'
import { reduxForm } from 'redux-form'
import PropTypes from 'prop-types'
import { useTranslation } from 'react-i18next'

// eslint-disable-next-line import/no-cycle
import Form from './Form'
import ReduxField from './ReduxField'

/**
 *
 * @reactProps {object} props.prompt - флаг включения обработки выхода с несохраненной формы
 * @reactProps {object} props.dirty - флаг отличия формы от initialState
 * @returns {*}
 * @constructor
 */
function ReduxForm({ prompt, dirty, ...props }) {
    const { t } = useTranslation()

    return (
        <>
            {prompt && (
                <Prompt when={dirty} message={t('defaultPromptMessage')} />
            )}
            <Form {...props} />
        </>
    )
}

ReduxForm.propTypes = {
    prompt: PropTypes.bool,
    dirty: PropTypes.bool,
}

ReduxForm.defaultProps = {
    prompt: false,
}

ReduxForm.Field = ReduxField

export default reduxForm({
    enableReinitialize: true,
})(ReduxForm)
