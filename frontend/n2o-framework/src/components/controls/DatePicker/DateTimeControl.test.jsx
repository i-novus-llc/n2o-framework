import React from 'react'
import sinon from 'sinon'
import moment from 'moment'

import DatePicker from './DateTimeControl'
import PopUp from './PopUp'
import Calendar from './Calendar'

const setup = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }

    const wrapper = mount(<DatePicker {...props} />)

    return {
        props,
        wrapper,
    }
}

const setupCalendar = (propOverrides) => {
    const props = {
    // use this to assign some default props
        locale: 'ru',
        ...propOverrides,
    }

    const wrapper = mount(<Calendar {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<DateTimeControl />', () => {
    it('видимость календаря определяется isCalendarVisible', () => {
        const { wrapper } = setup()

        expect(wrapper.find(PopUp)).toHaveLength(0)
        wrapper.setState({ isPopUpVisible: true })
        expect(wrapper.find(PopUp)).toHaveLength(1)
    })

    it('устанавливает value для календаря', () => {
        const { wrapper } = setup({
            value: '11/11/1111',
            dateFormat: 'DD/MM/YYYY',
        })

        expect(
            wrapper
                .find('DateInput')
                .first()
                .state().value,
        ).toBe('11/11/1111')
    })

    it('устанавливает disabled', () => {
        const { wrapper } = setup({ disabled: true })

        expect(
            wrapper
                .find('input')
                .first()
                .props().disabled,
        ).toBe(true)
        expect(
            wrapper
                .find('button')
                .first()
                .props().disabled,
        ).toBe(true)
    })

    it('устанавливает placeholder', () => {
        const { wrapper } = setup({ placeholder: 'test' })

        wrapper.setState({ isCalendarVisible: true })
        expect(
            wrapper
                .find('input')
                .first()
                .props().placeholder,
        ).toBe('test')
    })

    it('вызывает onChange, если значение инпута поменялось на корректную дату', () => {
        const onChange = sinon.spy()
        const { wrapper } = setup({ onChange, dateFormat: 'DD/MM/YYYY' })

        wrapper
            .find('input')
            .simulate('change', { target: { value: 'invalid date' } })
        expect(onChange.calledOnce).toEqual(false)
        wrapper
            .find('input')
            .simulate('change', { target: { value: '11/11/1111' } })
        expect(onChange.calledOnce).toEqual(true)
    })

    it('проверка onFocus своиства контролла', () => {
        const onFocus = sinon.spy()
        const { wrapper } = setup({ onFocus, dateFormat: 'DD/MM/YYYY' })

        wrapper.find('input').simulate('focus')

        expect(onFocus.calledOnce).toEqual(true)
    })

    it('корректно устанавливает формат даты (dateFormat)', () => {
        const { wrapper } = setup({
            value: moment('11/11/1111', 'DD/MM/YYYY'),
            dateDivider: ' ',
            timeFormat: 'HH:mm',
            dateFormat: 'DD/MM/YYYY',
        })

        // дефолтное время 00:00
        expect(
            wrapper
                .find('DateInput')
                .first()
                .state().value,
        ).toBe('11/11/1111 00:00')
    })

    it('делает неактивными кнопки до min', () => {
        const { wrapper } = setup({
            min: moment(),
            value: moment('11/11/1111', 'DD/MM/YYYY'),
        })

        wrapper.setState({ isPopUpVisible: true })
        expect(
            wrapper
                .find('td')
                .at(16)
                .hasClass('disabled'),
        ).toBe(true)
    })

    it('делает неактивными кнопки после max', () => {
        const { wrapper } = setup({
            max: moment(),
            value: moment('22/12/2030', 'DD/MM/YYYY'),
        })

        wrapper.setState({ isPopUpVisible: true })
        expect(
            wrapper
                .find('td')
                .at(16)
                .hasClass('disabled'),
        ).toBe(true)
    })

    it('проверяет, что дефолтное время 00:00', () => {
        const { wrapper } = setup({
            value: moment('22/12/2017', 'DD/MM/YYYY'),
            timeFormat: 'HH:mm',
            dateFormat: 'DD/MM/YYYY',
        })

        expect(
            wrapper
                .find('DateInput')
                .first()
                .state().value,
        ).toBe('22/12/2017 00:00')
    })

    it('устанавливает время', () => {
        const { wrapper } = setup({
            value: '1927-01-21T00:00:00',
            dateFormat: 'DD.MM.YYYY',
            outputFormat: 'YYYY-MM-DDTHH:mm:ss',
        })

        expect(
            wrapper
                .find('DateInput')
                .first()
                .state().value,
        ).toBe('21.01.1927')
    })

    it('устанавливает value', () => {
        const { wrapper } = setup({
            value: '11/11/1111',
            dateFormat: 'DD/MM/YYYY',
        })

        expect(
            wrapper
                .find('DateInput')
                .first()
                .state().value,
        ).toBe('11/11/1111')
    })

    it('в календарь приходит та же дата, что и в ДейтТаймКонтрол', () => {
        const { wrapper } = setup({
            value: '11/11/1111',
            dateFormat: 'DD/MM/YYYY',
        })

        wrapper.setState({ isPopUpVisible: true })
        expect(
            wrapper
                .find(Calendar)
                .first()
                .props()
                .value.format('DD/MM/YYYY'),
        ).toBe('11/11/1111')
    })
})

describe('<Calendar />', () => {
    it('в displayesMonth хранится начало месяца из пропертис', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        expect(
            wrapper.state('displayesMonth').diff(moment().startOf('month'), 'seconds'),
        ).toBe(0)
    })

    it('при клике на стрелку вправо отображается следующий месяц', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.fa.fa-angle-right').simulate('click')
        wrapper.update()
        expect(wrapper.state('displayesMonth').format('DD/MM/YYYY')).toBe(
            moment()
                .startOf('month')
                .add(1, 'months')
                .format('DD/MM/YYYY'),
        )
    })

    it('при клике на стрелку влево отображается следующий месяц', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.fa.fa-angle-left').simulate('click')
        wrapper.update()
        expect(wrapper.state('displayesMonth').format('DD/MM/YYYY')).toBe(
            moment()
                .startOf('month')
                .subtract(1, 'months')
                .format('DD/MM/YYYY'),
        )
    })

    it('при клике на стрелку влево отображается предыддущий год (если, отображаются месяцы)', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-month-title').simulate('click')
        wrapper.update()
        wrapper.find('.fa.fa-angle-left').simulate('click')
        expect(wrapper.state('displayesMonth').format('DD/MM/YYYY')).toBe(
            moment()
                .startOf('month')
                .subtract(1, 'years')
                .format('DD/MM/YYYY'),
        )
    })

    it('при клике на стрелку вправо отображается следующий год (если, отображаются месяцы)', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-month-title').simulate('click')
        wrapper.update()
        wrapper.find('.fa.fa-angle-right').simulate('click')
        expect(wrapper.state('displayesMonth').format('DD/MM/YYYY')).toBe(
            moment()
                .startOf('month')
                .add(1, 'years')
                .format('DD/MM/YYYY'),
        )
    })

    it('при клике на стрелку влево отображается предыддущее десятилетие (если, отображаются годы)', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-year-title').simulate('click')
        wrapper.update()
        wrapper.find('.fa.fa-angle-left').simulate('click')
        expect(wrapper.state('displayesMonth').format('DD/MM/YYYY')).toBe(
            moment()
                .startOf('month')
                .subtract(10, 'years')
                .format('DD/MM/YYYY'),
        )
    })

    it('при клике на стрелку вправо отображается следующее десятилетие (если, отображаются годы)', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-year-title').simulate('click')
        wrapper.update()
        wrapper.find('.fa.fa-angle-right').simulate('click')
        expect(wrapper.state('displayesMonth').format('DD/MM/YYYY')).toBe(
            moment()
                .startOf('month')
                .add(10, 'years')
                .format('DD/MM/YYYY'),
        )
    })

    it('при нажатии на название месяца вид календаря - по месяцам', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-month-title').simulate('click')
        wrapper.update()
        expect(wrapper.state('calendarType')).toBe(Calendar.BY_MONTHS)
    })

    it('при нажатии на год вид календаря - по годам', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-year-title').simulate('click')
        wrapper.update()
        expect(wrapper.state('calendarType')).toBe(Calendar.BY_YEARS)
    })

    it('при нажатии на название месяца календаря отображаются месяцы', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-month-title').simulate('click')
        wrapper.update()
        expect(wrapper.find('.n2o-calendar-body-item.month-item')).toHaveLength(12)
        expect(
            wrapper
                .find('.n2o-calendar-body-item.month-item')
                .at(0)
                .text(),
        ).toBe('январь')
    })

    it('при нажатии на название месяца календаря отображаются месяцы при value null', () => {
        const { wrapper } = setupCalendar({ value: null })

        wrapper.find('.n2o-calendar-header-month-title').simulate('click')
        wrapper.update()
        expect(wrapper.find('.n2o-calendar-body-item.month-item')).toHaveLength(12)
        expect(
            wrapper
                .find('.n2o-calendar-body-item.month-item')
                .at(0)
                .text(),
        ).toBe('январь')
    })

    it('при нажатии на год отображаются годы', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-year-title').simulate('click')
        wrapper.update()
        expect(wrapper.find('.n2o-calendar-body-item.year-item')).toHaveLength(12)
        expect(
            wrapper
                .find('.n2o-calendar-body-item.year-item')
                .at(0)
                .text(),
        ).toEqual((parseInt(moment().year() / 10) * 10 - 1).toString())
    })

    it('при клике на месяц из списка отображается кликнутый месяц', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-month-title').simulate('click')
        wrapper
            .find('.n2o-calendar-body-item.month-item')
            .at(0)
            .simulate('click')
        expect(wrapper.state('displayesMonth').month()).toBe(0)
    })

    it('при клике на год из списка отображается кликнутый год', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        wrapper.find('.n2o-calendar-header-year-title').simulate('click')
        wrapper
            .find('.n2o-calendar-body-item.year-item')
            .at(0)
            .simulate('click')
        expect(wrapper.state('displayesMonth').year()).toBe(
            parseInt(moment().year() / 10) * 10 - 1,
        )
    })

    // timepicker

    it('нет timeFormat - нет таймпикера', () => {
        const { wrapper } = setupCalendar({ value: moment() })

        expect(wrapper.find('.n2o-calendar-time-container')).toHaveLength(0)
    })

    it('есть timeFormat - есть таймпикер', () => {
        const { wrapper } = setupCalendar(
            { value: moment(), timeFormat: 'HH:mm:ss' },
            true,
        )

        expect(wrapper.find('.n2o-calendar-time-container')).toHaveLength(1)
    })

    // TODO настроить i18n для тестов
    // it('нет дефолтного времени - надпись "Выберите время"', () => {
    //   const { wrapper } = setupCalendar(
    //     { value: moment(), timeFormat: 'HH:mm:ss' },
    //     true
    //   );
    //   expect(wrapper.find('.n2o-calendar-time-container').text()).toBe(
    //     'Выберите время'
    //   );
    // });

    it('есть дефолтное время - отображается дефолтное время"', () => {
        const value = moment()
        const timeFormat = 'HH:mm:ss'
        const { wrapper } = setupCalendar(
            { value, timeFormat, hasDefaultTime: true },
            true,
        )

        expect(wrapper.find('.n2o-calendar-time-container').text()).toBe(
            value.format(timeFormat),
        )
    })
})
