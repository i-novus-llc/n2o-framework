import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import cloneDeep from 'lodash/cloneDeep'
import { getFormValues } from 'redux-form'
import { createStructuredSelector } from 'reselect'
import { connect } from 'react-redux'
import PropTypes from 'prop-types'
import React from 'react'

import { getFieldsKeys } from './utils'
import ReduxForm from './ReduxForm'

export const mapStateToProps = createStructuredSelector({
    reduxFormValues: (state, props) => getFormValues(props.id)(state) || {},
})

class Container extends React.Component {
    constructor(props) {
        super(props)
        const { models, form } = props
        const { resolve, datasource } = models
        const initialValues = cloneDeep(isEmpty(resolve) ? datasource?.[0] : resolve)
        const fields = getFieldsKeys(form.fieldsets)

        this.state = {
            initialValues,
            fields,
        }
    }

    componentDidUpdate({ models: prevModels, reduxFormValues: prevValues }) {
        const { models, reduxFormValues, setResolve } = this.props
        const { resolve, datasource } = models

        if (!isEqual(datasource, prevModels.datasource)) {
            setResolve(cloneDeep(datasource?.[0]))
        } else if (
            !isEqual(reduxFormValues, prevValues) &&
            !isEqual(reduxFormValues, resolve)
        ) {
            setResolve({
                ...(resolve || {}),
                ...reduxFormValues,
            })
        } else if (!isEqual(resolve, prevModels.resolve)) {
            this.setState({ initialValues: cloneDeep(resolve) })
        }
    }

    onChange = (/* values, dispatch, options, prevValues */) => {
        const {
            setActive, isActive,
        } = this.props

        if (setActive && !isActive) {
            setActive()
        }
    }

    render() {
        const { initialValues, fields } = this.state
        const { id, form, models } = this.props
        const { resolve } = models

        return (
            <ReduxForm
                form={id}
                fields={fields}
                {...form}
                activeModel={resolve}
                initialValues={initialValues}
                onChange={this.onChange}
            />
        )
    }
}

Container.propTypes = {
    form: PropTypes.shape({
        fetchOnInit: PropTypes.bool,
        fieldsets: PropTypes.array,
        prompt: PropTypes.bool,
    }),
    id: PropTypes.string.isRequired,
    setActive: PropTypes.func,
    setResolve: PropTypes.func,
    isActive: PropTypes.bool,
    models: PropTypes.object,
    reduxFormValues: PropTypes.object,
}

/**
 * Обертка в widgetContainer, мэппинг пропсов
 */
export default connect(mapStateToProps)(Container)
