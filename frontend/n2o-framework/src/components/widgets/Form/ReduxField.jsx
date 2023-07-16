import React from 'react'
import PropTypes from 'prop-types'
import some from 'lodash/some'

import withObserveDependency from '../../../core/dependencies/withObserveDependency'
import { Controller } from '../../core/FormProvider'

import withFieldContainer from './fields/withFieldContainer'
import StandardField from './fields/StandardField/StandardField'

// FIXME временное решение для fieldDependency type fetch,
//  вызывает _fetchData компонента, дергается в withObserveDependency
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

class ReduxField extends React.Component {
    constructor(props) {
        super(props)

        this.setRef = this.setRef.bind(this)
        this.Field = withFieldContainer(props.component)
    }

    setRef(el) {
        this.controlRef = el
    }

    render() {
        const { name } = this.props
        const Component = this.Field

        return (
            <Controller
                name={name}
                render={({ field }) => (
                    <Component
                        {...this.props}
                        {...field}
                        setRef={this.setRef}
                    />
                )}
            />
        )
    }
}

ReduxField.displayName = 'ReduxField'

ReduxField.defaultProps = {
    component: StandardField,
}

ReduxField.propTypes = {
    id: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
    name: PropTypes.string,
    component: PropTypes.oneOfType([
        PropTypes.string,
        PropTypes.func,
        PropTypes.node,
    ]),
    setReRenderRef: PropTypes.func,
}

export default withObserveDependency(config)(ReduxField)
