import { describe, it } from "mocha";
import { expect } from "chai";
import { SECURITY_CHECK } from "src/authTypes";
import authProvider, { checkPermission } from "src/authProvider";

const user = {
  username: "test",
  roles: ["user", "admin"],
  permissions: ["read", "edit"]
};

const config = {
  test: {
    roles: [],
    permissions: [],
    usernames: [],
    denied: false,
    permitAll: false,
    authenticated: false,
    anonymous: false
  }
};

describe("Тесты функции проверки прав (checkPermission)", () => {
  it("не должен дать доступ (denied=true)", () => {
    const cfg = {
      ...config.test,
      denied: true
    };
    expect(checkPermission(cfg)).to.be.false;
  });
  it("должен дать доступ (permitAll=true)", () => {
    const cfg = {
      ...config.test,
      permitAll: true
    };
    expect(checkPermission(cfg)).to.be.true;
  });
  it("должен дать доступ, когда нет пользователя, но разрешен для анонимных", () => {
    const cfg = {
      ...config.test,
      anonymous: true
    };
    expect(checkPermission(cfg)).to.be.true;
  });
  it("не должен дать доступ, когда нет пользователя и не разрешен для анонимных", () => {
    const cfg = {
      ...config.test,
      anonymous: false
    };
    expect(checkPermission(cfg)).to.be.false;
  });
  it("не должен дать доступ, когда есть пользователь и разрешен для анонимных", () => {
    const cfg = {
      ...config.test,
      anonymous: true
    };
    expect(checkPermission(cfg, user)).to.be.false;
  });
  it("должен дать доступ, когда есть пользователь и authenticated=true", () => {
    const cfg = {
      ...config.test,
      authenticated: true
    };
    expect(checkPermission(cfg, user)).to.be.true;
  });
  it("не должен дать доступ, когда roles, permissions и usernames пустые", () => {
    const cfg = {
      ...config.test
    };
    expect(checkPermission(cfg, user)).to.be.false;
  });
  it("должен дать доступ, совпадение по roles", () => {
    const cfg = {
      ...config.test,
      roles: ["admin"],
      permissions: ["dummy"],
      usernames: ["dummy"]
    };
    expect(checkPermission(cfg, user)).to.be.true;
  });
  it("должен дать доступ - совпадение по permissions", () => {
    const cfg = {
      ...config.test,
      roles: ["dummy"],
      permissions: ["read"],
      usernames: ["dummy"]
    };
    expect(checkPermission(cfg, user)).to.be.true;
  });
  it("должен дать доступ - совпадение по usernames", () => {
    const cfg = {
      ...config.test,
      roles: ["dummy"],
      permissions: ["dummy"],
      usernames: ["test"]
    };
    expect(checkPermission(cfg, user)).to.be.true;
  });
  // it('не должен дать доступ - не найдено совпадений', () => {
  //   const cfg = {
  //     ...config.test,
  //     roles: ["dummy"],
  //     permissions: ["dummy"],
  //     usernames: ["dummy"],
  //   };
  //   expect(checkPermission(cfg, user)).to.be.false;
  // });
  // it('должен дать доступ - полное совпадение', () => {
  //   const cfg = {
  //     ...config.test,
  //     roles: ["user", "admin"],
  //     permissions: ["read", "edit"],
  //     usernames: ["test"],
  //   };
  //   expect(checkPermission(cfg, user)).to.be.true;
  // });
  // it('должен дать доступ - пустые кроме одной', () => {
  //   const cfg = {
  //     ...config.test,
  //     roles: [],
  //     permissions: ["edit"],
  //     usernames: [],
  //   };
  //   expect(checkPermission(cfg, user)).to.be.true;
  // });
  // it('не должен дать доступ - юзера нет', () => {
  //   const cfg = {
  //     ...config.test,
  //     roles: [],
  //     permissions: ["edit"],
  //     usernames: [],
  //   };
  //   expect(checkPermission(cfg, {})).to.be.false;
  // });
  // it('не должен дать доступ - в конфиге permission, у юзера нет', () => {
  //   const cfg = {
  //     permissions: ['edit']
  //   };
  //
  //   expect(checkPermission(cfg, { permissions: [] })).to.be.false;
  // });
});

describe("Тесты провайдера по типу SECURITY_CHECK", () => {
  it("не должен дать доступ - один объект пустой", async () => {
    try {
      await authProvider(SECURITY_CHECK, { config, user });
    } catch (e) {
      expect(e).to.be.equal("Нет доступа.");
    }
  });
  it("не должен дать доступ - два объекта, один подходит, другой нет", async () => {
    const config = {
      test: {
        roles: [],
        permissions: [],
        usernames: [],
        denied: false,
        permitAll: false,
        authenticated: false,
        anonymous: true
      },
      test2: {
        roles: [],
        permissions: [],
        usernames: [],
        denied: false,
        permitAll: true,
        authenticated: false,
        anonymous: true
      }
    };
    try {
      await authProvider(SECURITY_CHECK, { config, user });
    } catch (e) {
      expect(e).to.be.equal("Нет доступа.");
    }
  });
  it("должен дать доступ - 3 объекта которые подходят", async () => {
    const config = {
      test: {
        roles: [],
        permissions: [],
        usernames: [],
        denied: false,
        permitAll: true,
        authenticated: false,
        anonymous: true
      },
      test2: {
        roles: [],
        permissions: [],
        usernames: [],
        denied: false,
        permitAll: true,
        authenticated: false,
        anonymous: true
      },
      test3: {
        roles: [],
        permissions: [],
        usernames: [],
        denied: false,
        permitAll: true,
        authenticated: false,
        anonymous: true
      }
    };
    const res = await authProvider(SECURITY_CHECK, { config, user });
    expect(res).to.be.equal(config);
  });
});
