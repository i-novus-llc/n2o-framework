import { useDispatch } from 'react-redux'
import React, { useCallback, useContext, useEffect } from 'react'
import get from 'lodash/get'
import PropTypes from 'prop-types'

import { addComponent, register, removeComponent } from '../../ducks/datasource/store'
import { resolveExpression } from '../withItemsResolver/utils'
import { DataSourceContext } from '../../core/widget/context'

/* datasource register with a context key by the path */
export function WithContextDataSource(Component) {
    function Register(props) {
        const {
            datasources,
            datasource,
            fetchData,
            location,
            path,
            force,
            models,
            id,
        } = props
        const hasSource = datasources && datasource
        const { key: queryKey, value } = resolveExpression(location, path)

        const dispatch = useDispatch()
        const { setResolve } = useContext(DataSourceContext)

        /* HACK! frontend custom query matching parameters manually,
           the context key taken from the path to the sidebar */
        useEffect(() => {
            if (!hasSource) {
                return
            }

            const metaConfig = { ...datasources[datasource] }
            const queryMapping = get(metaConfig, 'provider.queryMapping', null)

            if (!queryMapping) {
                return
            }

            const key = `${datasource}_${queryKey}`

            const config = {
                ...metaConfig,
                provider: {
                    ...metaConfig.provider,
                    queryMapping: {
                        ...queryMapping,
                        [key]: { ...queryMapping[key], value },
                    },
                },
            }

            dispatch(register(datasource, config))
            dispatch(addComponent(datasource, id))
            fetchData({}, force)

            // eslint-disable-next-line consistent-return
            return () => {
                dispatch(removeComponent(datasource, id))
            }
            // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [value, queryKey])

        /* the current resolve ds model received by custom mapping */
        const model = models.datasource[0]
        const resolveModel = useCallback(() => setResolve(model), [setResolve, model])

        useEffect(() => {
            if (model) {
                resolveModel()
            }
        }, [model, resolveModel])

        return <Component {...props} queryKey={queryKey} value={value} />
    }

    Register.propTypes = {
        datasources: PropTypes.object,
        datasource: PropTypes.string,
        id: PropTypes.string,
        fetchData: PropTypes.func,
        location: PropTypes.string,
        path: PropTypes.string,
        force: PropTypes.bool,
        models: PropTypes.object,
    }

    return Register
}
