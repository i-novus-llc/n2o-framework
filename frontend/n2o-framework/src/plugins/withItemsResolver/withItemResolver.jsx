import React, { useEffect } from 'react'
import { useDispatch } from 'react-redux'
import PropTypes from 'prop-types'

import { WithDataSource } from '../../core/datasource/WithDataSource'
import { addComponent, register } from '../../ducks/datasource/store'

import { resolveItems } from './utils'

export const withItemsResolver = (Component) => {
    const WithItemsResolver = WithDataSource((props) => {
        const dispatch = useDispatch()
        const { menu, extraMenu, datasources, datasource, models, fetchData, queryKey, value, force } = props
        const datasourceIsEmpty = !datasources || !datasource

        useEffect(() => {
            // FIXME Удалить костыль. Не понятно зачем тут мутация мапингов с последующей доперерегистрацией датасурса
            // NNO-8219
            if (!datasources?.[datasource]?.provider?.queryMapping) {
                return
            }

            let config = { ...datasources[datasource] }

            config = {
                ...config,
                provider: {
                    ...config.provider,
                    queryMapping: {
                        ...config.provider.queryMapping,
                        [`${datasource}_${queryKey}`]: {
                            ...config.provider.queryMapping[`${datasource}_${queryKey}`],
                            value,
                        },
                    },
                },
            }

            dispatch(register(datasource, config))
            dispatch(addComponent(datasource, datasource))
            fetchData({}, force)
            // eslint-disable-next-line react-hooks/exhaustive-deps
        }, [value])

        if (datasourceIsEmpty) {
            return <Component {...props} />
        }

        const { items = [] } = menu
        const { items: extraItems = [] } = extraMenu

        const resolvedItems = resolveItems(items, models)
        const resolvedExtraItems = resolveItems(extraItems, models)

        return (
            <Component
                {...props}
                menu={{ ...menu, items: resolvedItems }}
                extraMenu={{ ...extraMenu, items: resolvedExtraItems }}
            />
        )
    })

    WithItemsResolver.propTypes = {
        datasource: PropTypes.string,
        menu: PropTypes.object,
        fetchData: PropTypes.func,
        models: PropTypes.object,
    }

    return WithItemsResolver
}
