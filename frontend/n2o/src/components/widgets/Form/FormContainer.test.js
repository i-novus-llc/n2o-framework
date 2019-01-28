import React from 'react';
import sinon from 'sinon';
import * as hocs from './FormContainer';
import configureMockStore from 'redux-mock-store';
import { Provider } from 'react-redux';

const NullComponent = () => null;
const FormContainerTest = hocs.default;

function setup(props, hocName) {
  const TestComponent = hocs[hocName](NullComponent);
  return mount(<TestComponent {...props} />);
}

function setupToProvider(props, hocName, overrideStore = {}) {
  const mockStore = configureMockStore();
  const store = mockStore({
    models: { resolve: {} },
    ...overrideStore
  });
  return mount(
    <Provider store={store}>
      <FormContainerTest {...props} />
    </Provider>
  );
}

describe('FormContainer', () => {
  describe('Проверка прокидвания пропсов withWidgetContainer', () => {
    it('Проверка при незаданных props', () => {
      const wrapper = setupToProvider({}, 'withWidgetContainer');
      expect(wrapper.find(FormContainerTest).exists()).toBeTruthy();
    });
  });

  describe('Проверка withLiveCycleMethods', () => {
    it('Создание компонента', () => {
      const wrapper = setup({}, 'withLiveCycleMethods');
      expect(wrapper.find(NullComponent).exists()).toBeTruthy();
    });
    it('Вызов setDefaultValues с активной моделью', () => {
      const setDefaultValues = sinon.spy();
      const wrapper = setup(
        {
          activeModel: 'activeModel',
          defaultValues: 'defaultValues',
          reduxFormValues: ['reduxFormValues'],
          setDefaultValues
        },
        'withLiveCycleMethods'
      );

      expect(setDefaultValues.calledOnce).toBe(false);

      wrapper.setProps({ activeModel: 'newActiveModel' }).update();
      expect(setDefaultValues.calledOnce).toBe(true);
      expect(setDefaultValues.calledWith('newActiveModel')).toBe(true);
    });

    it('Вызов setDefaultValues с null при изменении datasource', () => {
      const setDefaultValues = sinon.spy();
      const wrapper = setup(
        {
          defaultValues: 'defaultValues',
          datasource: [],
          setDefaultValues
        },
        'withLiveCycleMethods'
      );

      wrapper.setProps({ datasource: ['newValue'] }).update();
      expect(setDefaultValues.calledOnce).toBe(true);
      expect(setDefaultValues.calledWith(null)).toBe(true);
    });
  });

  describe('Проверка withPropsOnChangeWidget', () => {
    it('Создание компонента', () => {
      const wrapper = setup({}, 'withPropsOnChangeWidget');
      expect(wrapper.find(NullComponent).exists()).toBeTruthy();
    });
    it('Прокидывание initialValues при изменении isEnabled', () => {
      const wrapper = setup(
        {
          defaultValues: [],
          isEnabled: false
        },
        'withPropsOnChangeWidget'
      );
      wrapper.setProps({ isEnabled: true }).update();
      expect(wrapper.find(NullComponent).props()).toHaveProperty('initialValues', []);
    });

    it('Прокидывание initialValues при изменении defaultValues и isEnabled=true', () => {
      const wrapper = setup(
        {
          defaultValues: [],
          isEnabled: true
        },
        'withPropsOnChangeWidget'
      );
      wrapper.setProps({ defaultValues: ['newDefaultValue'] }).update();
      expect(wrapper.find(NullComponent).props()).toHaveProperty('initialValues', [
        'newDefaultValue'
      ]);
    });

    it('Прокидывание initialValues при изменении isEnabled, если нет defaultValues', () => {
      const wrapper = setup(
        {
          resolveModel: { resolve: '' },
          datasource: { data: '' },
          isEnabled: false
        },
        'withPropsOnChangeWidget'
      );
      wrapper.setProps({ isEnabled: true }).update();
      expect(wrapper.find(NullComponent).props()).toHaveProperty('initialValues', {
        resolve: '',
        data: ''
      });
    });

    it('initialValues = {} при изменении isEnabled с true в false', () => {
      const wrapper = setup(
        {
          defaultValues: [],
          isEnabled: true
        },
        'withPropsOnChangeWidget'
      );
      wrapper.setProps({ isEnabled: false }).update();
      expect(wrapper.find(NullComponent).props()).toHaveProperty('initialValues', {});
    });
  });

  describe('Проверка withWidgetHandlers', () => {
    it('проверка onChange метода. Вызов onResolve если пришел initialValues', () => {
      const onResolve = sinon.spy();
      const wrapper = setup(
        {
          initialValues: { init: 'test' },
          reduxFormValues: { init: 'test' },
          resolveModel: {},
          modelPrefix: 'datasource',
          onResolve,
          onSetModel: () => {}
        },
        'withWidgetHandlers'
      );
      wrapper
        .find(NullComponent)
        .props()
        .onChange('newValue');

      expect(onResolve.calledOnce).toBe(true);
      expect(onResolve.calledWith({ init: 'test' })).toBe(true);
    });

    it('onResolve нет values и modelPrefix', () => {
      const onResolve = sinon.spy();
      const wrapper = setup(
        {
          onResolve
        },
        'withWidgetHandlers'
      );
      wrapper
        .find(NullComponent)
        .props()
        .onChange('newValue');

      expect(onResolve.calledOnce).toBe(true);
      expect(onResolve.calledWith('newValue')).toBe(true);
    });

    it('onSetModel если reduxFormValues и prevValue не совпадают', () => {
      const onSetModel = sinon.spy();
      const wrapper = setup(
        {
          onSetModel,
          reduxFormValues: { init: 'test' },
          modelPrefix: 'datasource',
          onResolve: () => {}
        },
        'withWidgetHandlers'
      );
      wrapper
        .find(NullComponent)
        .props()
        .onChange('newValue', null, null, 'prevValue');

      expect(onSetModel.calledOnce).toBe(true);
      expect(onSetModel.calledWith('newValue')).toBe(true);
    });
  });

  it('Проверка compose', () => {
    const wrapper = setupToProvider(null, 'default');

    expect(
      wrapper
        .find(
          'withProps(Connect(withState(lifecycle(withPropsOnChange(withHandlers(onlyUpdateForKeys(ReduxForm)))))))'
        )
        .exists()
    ).toBe(true);
  });
});
