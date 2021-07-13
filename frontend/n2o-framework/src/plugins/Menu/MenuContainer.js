import React from 'react'
import PropTypes from 'prop-types'
import { getContext, compose, mapProps } from 'recompose'
import isArray from 'lodash/isArray'
import isEqual from 'lodash/isEqual'
import uniqBy from 'lodash/uniqBy'
import isEmpty from 'lodash/isEmpty'
import filter from 'lodash/filter'
import get from 'lodash/get'
import toArray from 'lodash/toArray'
import findIndex from 'lodash/findIndex'
import { withRouter } from 'react-router-dom'

import withSecurity from '../../core/auth/withSecurity'
import { SECURITY_CHECK } from '../../core/auth/authTypes'

export class MenuContainer extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            items: [],
            extraItems: [],
        }

        this.mapRenderProps = this.mapRenderProps.bind(this)
        this.checkItem = this.checkItem.bind(this)
        this.makeSecure = this.makeSecure.bind(this)
        this.getItemsWithAccess = this.getItemsWithAccess.bind(this)
        this.setSubItem = this.setSubItem.bind(this)
    }

    async componentDidMount() {
        await this.getItemsWithAccess()
    }

    async componentDidUpdate(prevProps) {
        const { user } = this.props
        /*
        * TODO сделать обертку которая передает items
        *  исбавиться от get
        */
        const items = get(this.props, 'header.menu.items')
        const prevItems = get(prevProps, 'header.menu.items')
        const extraItems = get(prevProps, 'header.extraMenu.items')
        const prevExtraItems = get(prevProps, 'header.extraMenu.items')

        if (
            !isEqual(items, prevItems) ||
            !isEqual(extraItems, prevExtraItems) ||
            !isEqual(user, prevProps.user)
        ) {
            await this.getItemsWithAccess()
        }
    }

    async checkItem(item, type, id = null) {
        if (item.security) {
            const { user, authProvider } = this.props
            const config = item.security

            try {
                await authProvider(SECURITY_CHECK, {
                    config,
                    user,
                })

                if (!id) {
                    this.setState(prevState => ({
                        ...prevState,
                        [type]: toArray(uniqBy(prevState[type].concat(item), 'id')),
                    }))
                }
            } catch (error) {
                if (id) {
                    this.setSubItem(item, type, id)
                }
            }
        } else {
            if (!id) {
                this.setState(prevState => ({
                    ...prevState,
                    [type]: toArray(uniqBy(prevState[type].concat(item), 'id')),
                }))
            }
            if (item.items) {
                // eslint-disable-next-line no-restricted-syntax
                for (const subItem of item.items) {
                    await this.checkItem(subItem, 'items', item.id)
                }
            }
        }
    }

    setSubItem(item, type, id) {
        const { [type]: stateItem } = this.state

        const parentIndex = findIndex(stateItem, i => i.id === id)
        const parentItem = get(stateItem, parentIndex.toString())
        let subItems = get(parentItem, 'items', [])

        subItems = filter(subItems, i => i.id !== item.id)
        parentItem.items = subItems
        this.setState((prevState) => {
            const newState = prevState

            newState[type][parentIndex].items = subItems

            return newState
        })
    }

    async makeSecure(config) {
        const makeSecure = async (items, type) => {
            if (isArray(items) && !isEmpty(items)) {
                // eslint-disable-next-line no-restricted-syntax
                for (const item of items) {
                    await this.checkItem(item, type)
                }
            }
        }
        /*
        * TODO сделать обертку которая передает items
        *  исбавиться от get
        */

        const items = get(config, 'header.menu.items')
        const extraItems = get(config, 'header.extraMenu.items')

        await makeSecure(items, 'items')
        await makeSecure(extraItems, 'extraItems')
    }

    async getItemsWithAccess() {
        await this.setState({ items: [], extraItems: [] })
        await this.makeSecure(this.props)
    }

    mapRenderProps() {
        const { items, extraItems } = this.state
        const { header } = this.props

        if (!header) {
            return this.props
        }

        const headerProps = {
            header: {
                ...header,
                menu: {
                    items: filter(items, i => !i.items || !isEmpty(i.items)),
                },
                extraMenu: {
                    items: filter(extraItems, i => !i.items || !isEmpty(i.items)),
                },
            },
        }

        return {
            ...this.props,
            ...headerProps,
        }
    }

    render() {
        const { render } = this.props

        return render(this.mapRenderProps())
    }
}

MenuContainer.propTypes = {
    render: PropTypes.func,
    user: PropTypes.any,
    authProvider: PropTypes.any,
    header: PropTypes.object,
}

MenuContainer.defaultProps = {
    render: () => {},
}

export const ConfigContainer = compose(
    getContext({
        getFromConfig: PropTypes.func,
    }),
    withRouter,
    withSecurity,
    mapProps(({ getFromConfig, ...rest }) => ({
        ...rest,
        ...(getFromConfig && {
            ...getFromConfig('menu'),
        }),
    })),
)(MenuContainer)

export default ConfigContainer
