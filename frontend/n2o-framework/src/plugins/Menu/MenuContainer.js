import React from 'react'
import PropTypes from 'prop-types'
import { getContext, compose, mapProps } from 'recompose'
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

const initialItems = {
    headerItems: [],
    headerExtraItems: [],
    sidebarItems: [],
    sidebarExtraItems: [],
}

export class MenuContainer extends React.Component {
    state = {
        ...initialItems,
    }

    async componentDidMount() {
        await this.getItemsWithAccess()
    }

    async componentDidUpdate(prevProps) {
        const {
            user,
            headerItems,
            headerExtraItems,
            sidebarItems,
            sidebarExtraItems,
        } = this.props

        const {
            headerItems: prevHeaderItems,
            headerExtraItems: prevHeaderExtraItems,
            sidebarItems: prevSidebarItems,
            sidebarExtraItems: prevSidebarExtraItems,
            user: prevPropsUser,
        } = prevProps

        if (
            !isEqual(headerItems, prevHeaderItems) ||
            !isEqual(headerExtraItems, prevHeaderExtraItems) ||
            !isEqual(sidebarItems, prevSidebarItems) ||
            !isEqual(sidebarExtraItems, prevSidebarExtraItems) ||
            !isEqual(user, prevPropsUser)
        ) {
            await this.getItemsWithAccess()
        }
    }

     checkItem = async (item, type, id = null) => {
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
                 for (const subItem of item.items) {
                     await this.checkItem(subItem, 'items', item.id)
                 }
             }
         }
     }

     setSubItem = (item, type, id) => {
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

     makeSecure = async (headerItems, headerExtraItems, sidebarItems, sidebarExtraItems) => {
         const makeSecure = async (items, type) => {
             if (Array.isArray(items) && !isEmpty(items)) {
                 for (const item of items) {
                     await this.checkItem(item, type)
                 }
             }
         }

         await makeSecure(headerItems, 'headerItems')
         await makeSecure(headerExtraItems, 'headerExtraItems')
         await makeSecure(sidebarItems, 'sidebarItems')
         await makeSecure(sidebarExtraItems, 'sidebarExtraItems')
     }

      getItemsWithAccess = async () => {
          const {
              headerItems,
              headerExtraItems,
              sidebarItems,
              sidebarExtraItems,
          } = this.props

          await this.setState({ ...initialItems })
          await this.makeSecure(headerItems, headerExtraItems, sidebarItems, sidebarExtraItems)
      }

      mapRenderProps = () => {
          const { headerItems, headerExtraItems, sidebarItems, sidebarExtraItems } = this.state
          const { header, sidebar, location } = this.props

          if (!header && !sidebar) {
              return this.props
          }
          console.log('Yo!!!', this.props)
          const withSecurityProps = (props, items, extraItems, type) => ({
              [type]: {
                  ...props,
                  location,
                  menu: {
                      items: filter(items, i => !i.items || !isEmpty(i.items)),
                  },
                  extraMenu: {
                      items: filter(extraItems, i => !i.items || !isEmpty(i.items)),
                  },
              },
          })

          return {
              ...this.props,
              ...withSecurityProps(header, headerItems, headerExtraItems, 'header'),
              ...withSecurityProps(sidebar, sidebarItems, sidebarExtraItems, 'sidebar'),
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
    sidebar: PropTypes.object,
    location: PropTypes.object,
    headerItems: PropTypes.array,
    headerExtraItems: PropTypes.array,
    sidebarItems: PropTypes.array,
    sidebarExtraItems: PropTypes.array,
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
    mapProps(({ getFromConfig, ...rest }) => {
        let configProps = {}

        if (getFromConfig) {
            configProps = getFromConfig('menu')
        }

        const { header, sidebar } = configProps

        return {
            ...rest,
            ...configProps,
            headerItems: get(header, 'menu.items') || [],
            headerExtraItems: get(header, 'extraMenu.items') || [],
            sidebarItems: get(sidebar, 'menu.items') || [],
            sidebarExtraItems: get(sidebar, 'extraMenu.items') || [],
        }
    }),
)(MenuContainer)

export default ConfigContainer
