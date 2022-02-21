import React from 'react'
import PropTypes from 'prop-types'
import { connect, ReactReduxContext } from 'react-redux'
import { createStructuredSelector } from 'reselect'
import { Field as ReduxFormField } from 'redux-form'
import { compose, withProps } from 'recompose'
import some from 'lodash/some'

import withObserveDependency from '../../../core/dependencies/withObserveDependency'
import { loadingSelector } from '../../../ducks/form/selectors'

import withFieldContainer from './fields/withFieldContainer'
import StandardField from './fields/StandardField/StandardField'

const config = {
    // FIXME Непонятно почему зависимость 'fetch' реализована тут, отдельно от всех остальных. Надо свести всё в единое место
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
    constructor(props) {
        super(props)

        this.setRef = this.setRef.bind(this)
        const Field = compose(
            withProps(() => ({
                setReRenderRef: props.setReRenderRef,
            })),
            withFieldContainer,
        )(props.component)

        // фикс для проброса валидации через redux-field, который ожидает function/function[], а мы используем это как string[] для валидации датасурсов
        // eslint-disable-next-line react/destructuring-assignment, react/prop-types
        this.Field = props => (<Field {...props} validate={this.props.validate} />)
    }

    setRef(el) {
        this.controlRef = el
    }

    render() {
        const { id } = this.props

        return (
            <ReduxFormField
                name={id}
                {...this.props}
                component={this.Field}
                setRef={this.setRef}
                validate={undefined}
            />
        )
    }
}

ReduxField.contextType = ReactReduxContext

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
