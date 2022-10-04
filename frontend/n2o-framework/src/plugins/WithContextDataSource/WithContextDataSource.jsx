import { useDispatch } from 'react-redux'
import React, { useCallback, useContext, useEffect } from 'react'
import get from 'lodash/get'
import PropTypes from 'prop-types'

import { addComponent, register, removeComponent } from '../../ducks/datasource/store'
import { resolveExpression } from '../withItemsResolver/utils'
import { DataSourceContext } from '../../core/widget/context'

import { queryMappingResolver } from './utils'

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
        /*
           HACK!
           Frontend custom query matching parameters manually,
           The queryKey shows which part of the URL the value should be extracted from.
           The context value taken from the location using the queryKey.
        */

        const hasSource = datasources && datasource
        const { key: queryKey, value } = resolveExpression(location, path)

        const dispatch = useDispatch()
        const { setResolve } = useContext(DataSourceContext)

        useEffect(() => {
            if (!hasSource) {
                return
            }

            const metaConfig = { ...datasources[datasource] }

            /* unresolved meta query mapping */
            const metaQueryMapping = get(metaConfig, 'provider.queryMapping', null)

            if (!metaQueryMapping) {
                return
            }

            const dsQueryKey = `${datasource}_${queryKey}`
            const dsQueryMapping = { [dsQueryKey]: { ...metaQueryMapping[dsQueryKey], value } }

            const queryMapping = queryMappingResolver(metaQueryMapping, value, dsQueryMapping)

            const config = { ...metaConfig, provider: { ...metaConfig.provider, queryMapping } }

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
