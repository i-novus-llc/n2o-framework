import React from 'react'
import some from 'lodash/some'

import withObserveDependency from '../../../core/dependencies/withObserveDependency'
import { Controller } from '../../core/FormProvider'
import { DependencyTypes, DataSourceDependencyBase } from '../../../core/datasource/const'

import withFieldContainer from './fields/withFieldContainer'
import { StandardField } from './fields/StandardField/StandardField'

interface ControlProps {
    fetchData(params: { size: number }): void
    size: number
    labelFieldId: string
}

interface ControlRef {
    props: ControlProps
}

interface Config {
    controlRef?: ControlRef
    onChange(params: { dependency: DataSourceDependencyBase[] }, dependencyType: DependencyTypes): void
}

// FIXME временное решение для fieldDependency type fetch,
//  вызывает _fetchData компонента, дергается в withObserveDependency

const config: Config = {
    onChange({ dependency }, dependencyType) {
        if (!this.controlRef) { return }

        const { fetchData, size, labelFieldId } = this.controlRef.props
        const haveReRenderDependency = some(dependency, { type: dependencyType })

        if (haveReRenderDependency) {
            fetchData({
                size,
                [`sorting.${labelFieldId}`]: 'ASC',
            })
        }
    },
}

interface ReduxFieldProps {
    name: string
    component: React.ComponentType<Record<string, unknown>>
}

class ReduxField extends React.Component<ReduxFieldProps> {
    controlRef: HTMLElement | null = null

    Field: React.ComponentType<Record<string, unknown>>

    static displayName = 'ReduxField'

    static defaultProps = {
        component: StandardField,
    }

    constructor(props: ReduxFieldProps) {
        super(props)

        this.setRef = this.setRef.bind(this)
        this.Field = withFieldContainer(props.component)
    }

    setRef(el: HTMLElement | null) {
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

export default withObserveDependency(config)(ReduxField)
