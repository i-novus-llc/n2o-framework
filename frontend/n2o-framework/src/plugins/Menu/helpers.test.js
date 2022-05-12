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

const sidebarWithComplexAsteriskPath = {
    className: "",
    defaultState: "maxi",
    extraMenu: {},
    logo: {title: 'Получатель услуг', href: '/'},
    menu: {items: Array(0)},
    overlay: false,
    path: "/persons/*/list",
    side: "left",
    src: "Sidebar",
    toggleOnHover: false,
    toggledState: "mini"
}

const sidebarWithDoubleAsteriskPath = {
    className: "",
    defaultState: "maxi",
    extraMenu: {},
    logo: {title: 'Получатели', href: '/'},
    menu: {items: Array(0)},
    overlay: false,
    path: "/persons/**",
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

    it('вернет более релевантный sidebar в случае с динамическими путями', () => {
        expect(
            getMatchingSidebar([sidebarWithComplexAsteriskPath, sidebarWithDoubleAsteriskPath], '/persons/1/list').logo
                .title
        ).toEqual(sidebarWithComplexAsteriskPath.logo.title)
    })

    it('не вернет sidebar, если в указано больше ресурсов, чем ожидается в пути', () => {
        expect(isEmpty(getMatchingSidebar([sidebarWithComplexAsteriskPath], '/persons/1/2/list'))).toEqual(true)
    })  
})
