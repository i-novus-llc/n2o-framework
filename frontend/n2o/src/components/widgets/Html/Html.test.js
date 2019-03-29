import React from 'react';
import sinon from 'sinon';
import Html from './Html';

const setup = propOverrides => {
  const props = Object.assign(
    {
      // use this to assign some default props
    },
    propOverrides
  );

  const wrapper = mount(<Html {...props} />);

  return {
    props,
    wrapper
  };
};

describe('<Html />', () => {
  it('tests component existence', () => {
    const { wrapper } = setup({ url: 'mockHtml.html' });
    expect(wrapper.exists()).toBe(true);
  });

  it('contains html', () => {
    const wrapper = setup({ url: 'mockHtml.html' });
    const fakeHtml = <div>fake</div>;
    const promise = Promise.resolve(fakeHtml);
    sinon.stub(Html.prototype, 'handleResponse').callsFake(() => promise);
    promise.then(() => wrapper.update()).then(() => wrapper.contains(fakeHtml).toEqual(true));
  });
  it('calls componentDidMount', () => {
    sinon.spy(Html.prototype, 'componentDidMount');
    const wrapper = setup({ url: 'mockHtml.html' });
    expect(Html.prototype.componentDidMount.calledOnce).toEqual(true);
  });

  it('sends alert when didnt find page', done => {
    const fakeHtml = <div>fake</div>;
    const fakeResponse = { status: 404, text: () => Promise.resolve(fakeHtml) };
    const responsePromise = Promise.resolve(fakeResponse);
    const { wrapper } = setup({ url: 'mockHtml.html' });
    sinon.spy(Html.prototype, 'sendAlert');
    Html.prototype.handleResponse.restore();
    sinon.stub(Html.prototype, 'fetch').callsFake(() => responsePromise);
    wrapper.instance().handleResponse();
    responsePromise.then(() => {
      expect(Html.prototype.sendAlert.calledOnce).toBe(true);
      done();
    });
  });
});
