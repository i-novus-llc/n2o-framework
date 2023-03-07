import { breadcrumbResolver } from './breadcrumbResolver'

const model = {
    name: 'Alex',
    id: 'uid',
}

const breadcrumb = [
    {
        label: 'Первая страница пользователя',
        path: '/page1',
    },
    {
        label: '`\'Вторая страница пользователя с \'+id+\'\'`',
        path: '/page2',
    },
    {
        label: '`\'Третья страница пользователя \'+name+\'\'`',
    },
]

describe('<breadcrumbResolver>', () => {
    it('breadcrumb labels resolving', () => {
        const resolvedBreadcrumb = breadcrumbResolver(model, breadcrumb)

        expect(resolvedBreadcrumb.length).toBe(3)

        expect(resolvedBreadcrumb[0].label).toBe('Первая страница пользователя')
        expect(resolvedBreadcrumb[0].path).toBe('/page1')

        expect(resolvedBreadcrumb[1].label).toBe('Вторая страница пользователя с uid')
        expect(resolvedBreadcrumb[1].path).toBe('/page2')

        expect(resolvedBreadcrumb[2].label).toBe('Третья страница пользователя Alex')
        expect(resolvedBreadcrumb[2].path).toBe(undefined)
    })
})
