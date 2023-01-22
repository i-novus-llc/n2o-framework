import React, { useCallback } from 'react'
import PropTypes from 'prop-types'
import { connect } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import { Field as ReduxFormField } from 'redux-form'
import { compose, withProps } from 'recompose'
import some from 'lodash/some'

import withObserveDependency from '../../../core/dependencies/withObserveDependency'
import { loadingSelector } from '../../../ducks/form/selectors'

import withFieldContainer from './fields/withFieldContainer'
import StandardField from './fields/StandardField/StandardField'
import { useFormContainerContext } from './provider/FormContainerProvider'

const config = {
    onChange({ dependency }, dependencyType) {
        if (!this.controlRef) { return }
        const { _fetchData, size, labelFieldId } = this.controlRef.props
        const haveReRenderDependency = some(dependency, { type: dependencyType })

        if (haveReRenderDependency) {
            _fetchData({
                size,
                [`sorting.${labelFieldId}`]: 'ASC',
            })
        }
    },
}

/**
 * Поле для {@link ReduxForm}
 * @reactProps {number} id
 * @reactProps {node} component - компонент, который оборачивает поле. Дефолт: {@link StandardField}
 * @see https://redux-form.com/6.0.0-alpha.4/docs/api/field.md/
 * @example
 *
 */
class ReduxField extends React.Component {
    /**
   *Базовый рендер
   */
    constructor(props) {
        super(props)

        this.setRef = this.setRef.bind(this)
        this.Field = compose(
            withProps(() => ({
                setReRenderRef: props.setReRenderRef,
            })),
            withFieldContainer,
        )(props.component)
    }

    setRef(el) {
        this.controlRef = el
    }

    render() {
        const { id } = this.props
        const { store } = this.context

        return (
            <Field
                id={id}
                {...this.props}
                component={this.Field}
                setRef={this.setRef}
                dispatch={store.dispatch}
            />
        )
    }
}

const Field = ({ id, component, setRef, dispatch, ...otherProps }) => {
    const { onBlur } = useFormContainerContext()
    const handleBlur = useCallback(() => {
        onBlur(dispatch)
    }, [onBlur, dispatch])

    return (
        <ReduxFormField
            name={id}
            {...otherProps}
            component={component}
            onBlur={handleBlur}
            setRef={setRef}
        />
    )
}

Field.propTypes = {
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    component: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.func,
        PropTypes.node,
    ]),
    setRef: PropTypes.func,
    dispatch: PropTypes.func,
}

ReduxField.contextTypes = {
    store: PropTypes.object,
}

ReduxField.defaultProps = {
    component: StandardField,
}

ReduxField.propTypes = {
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    component: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.func,
        PropTypes.node,
    ]),
    setReRenderRef: PropTypes.func,
}

const mapStateToProps = createStructuredSelector({
    loading: (state, { form, id }) => loadingSelector(form, id)(state),
})

export default compose(
    connect(mapStateToProps),
    withObserveDependency(config),
)(ReduxField)
