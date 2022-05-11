import isEmpty from 'lodash/isEmpty'
import { getMatchingSidebar } from './helpers'

const sidebarWithoutPath = {
    className: "",
    defaultState: "maxi",
    extraMenu: {},
    logo: {title: 'N2O', href: '/'},
    menu: {items: Array(0)},
    overlay: false,
    side: "left",
    src: "Sidebar",
    toggleOnHover: false,
    toggledState: "mini"
}

const sidebarWithSpecifiedPath = {
    className: "",
    defaultState: "maxi",
    extraMenu: {},
    logo: {title: 'Получатель услуг', href: '/'},
    menu: {},
    overlay: false,
    path: "/person",
    side: "left",
    src: "Sidebar",
    subtitle: "{name}",
    toggleOnHover: false,
    toggledState: "mini"
}

const sidebarWithAsteriskPath = {
    className: "",
    defaultState: "maxi",
    extraMenu: {},
    logo: {title: 'N2O', href: '/'},
    menu: {items: Array(0)},
    overlay: false,
    path: "/*",
    side: "left",
    src: "Sidebar",
    toggleOnHover: false,
    toggledState: "mini"
}

describe('getMatchingSidebar', () => {
    it('вернет sidebar, если он один', () => {
        expect(getMatchingSidebar([sidebarWithoutPath], '/person').logo.title).toEqual(sidebarWithoutPath.logo.title)
    })

    it('вернет sidebar с указанным path, если есть sidebar без path', () => {
        expect(getMatchingSidebar([sidebarWithoutPath, sidebarWithSpecifiedPath], '/person').logo.title).toEqual(
            sidebarWithSpecifiedPath.logo.title
        )
    })

    it('вернет более релевантный sidebar, если есть несколько подходящих', () => {
        expect(getMatchingSidebar([sidebarWithSpecifiedPath, sidebarWithAsteriskPath], '/person').logo.title).toEqual(
            sidebarWithSpecifiedPath.logo.title
        )
    })

    it('не вернет sidebar, если нет подходящего', () => {
        expect(isEmpty(getMatchingSidebar([sidebarWithSpecifiedPath], '/settings'))).toEqual(true)
    })
})
