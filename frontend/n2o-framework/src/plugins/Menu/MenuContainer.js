import React from 'react'
import PropTypes from 'prop-types'
import { getContext, compose, mapProps } from 'recompose'
import isEqual from 'lodash/isEqual'
import isEmpty from 'lodash/isEmpty'
import get from 'lodash/get'
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

    giveAccess = (newItem, type, parentId, isChildren) => {
        /* isChildren dropdown children items */
        if (isChildren) {
            this.setState((prevState) => {
                const resolvedItems = prevState[type]

                const nextResolvedItems = resolvedItems.map((item) => {
                    const { id } = item

                    if (id === parentId) {
                        const { items = [] } = item

                        return {
                            ...item,
                            items: items.concat(newItem),
                        }
                    }

                    return item
                })

                return {
                    ...prevState,
                    [type]: nextResolvedItems,
                }
            })

            return
        }

        /* init state items, this is necessary for deep verification */
        const finalItem = newItem.type === 'dropdown'
            ? { ...newItem, items: [] }
            : newItem

        this.setState(prevState => ({
            ...prevState,
            [type]: prevState[type].concat(finalItem),
        }))
    }

     checkItem = async (item, type, parentId, isChildren = false) => {
         const { id, items } = item

         /* if the item is the parent, take its id (item.id)  */
         const currentId = parentId || id

         if (item.security) {
             const { user, authProvider } = this.props
             const config = item.security

             try {
                 await authProvider(SECURITY_CHECK, {
                     config,
                     user,
                 })

                 this.giveAccess(item, type, currentId, isChildren)
             } catch (error) {
                 /* nothing to do */
             }
         } else {
             this.giveAccess(item, type, currentId, isChildren)
         }

         if (items) {
             for (const subItem of items) {
                 await this.checkItem(subItem, type, currentId, true)
             }
         }
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
          const { header, sidebar, location, datasources = {} } = this.props

          if (!header && !sidebar) {
              return this.props
          }

          const withSecurityProps = (props, items, extraItems, type) => ({
              [type]: {
                  ...props,
                  datasources,
                  location,
                  menu: {
                      items,
                  },
                  extraMenu: extraItems,
              },
          })

          return {
              ...this.props,
              ...withSecurityProps(
                  header,
                  headerItems,
                  headerExtraItems,
                  'header',
              ),
              ...withSecurityProps(
                  sidebar,
                  sidebarItems,
                  sidebarExtraItems,
                  'sidebar',
              ),
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
    location: PropTypes.object,
    sidebar: PropTypes.object,
    datasources: PropTypes.object,
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
