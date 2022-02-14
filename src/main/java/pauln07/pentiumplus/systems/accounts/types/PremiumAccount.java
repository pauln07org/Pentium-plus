 /*
 * This file is part of the Pentium plus (https://github.com/pauln07org/Pentium-plus).
 * Copyright (c) 2022 Pentium plus.
 */

package pauln07.pentiumplus.systems.accounts.types;

import com.mojang.authlib.Agent;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import pauln07.pentiumplus.PentiumPlus;
import pauln07.pentiumplus.mixin.MinecraftClientAccessor;
import pauln07.pentiumplus.systems.accounts.Account;
import pauln07.pentiumplus.systems.accounts.AccountType;
import pauln07.pentiumplus.utils.misc.NbtException;
import net.minecraft.client.util.Session;
import net.minecraft.nbt.NbtCompound;

import java.util.Optional;

import static pauln07.pentiumplus.PentiumPlus.mc;

public class PremiumAccount extends Account<PremiumAccount> {
    private String password;

    public PremiumAccount(String name, String password) {
        super(AccountType.Premium, name);
        this.password = password;
    }

    @Override
    public boolean fetchInfo() {
        YggdrasilUserAuthentication auth = getAuth();

        try {
            auth.logIn();

            cache.username = auth.getSelectedProfile().getName();
            cache.uuid = auth.getSelectedProfile().getId().toString();

            return true;
        } catch (AuthenticationException e) {
            return false;
        }
    }

    @Override
    public boolean login() {
        super.login();

        YggdrasilUserAuthentication auth = getAuth();

        try {
            auth.logIn();
            setSession(new Session(auth.getSelectedProfile().getName(), auth.getSelectedProfile().getId().toString(), auth.getAuthenticatedToken(), Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));

            cache.username = auth.getSelectedProfile().getName();
            return true;
        } catch (AuthenticationUnavailableException e) {
            PentiumPlus.LOG.error("Failed to contact the authentication server.");
            return false;
        } catch (AuthenticationException e) {
            if (e.getMessage().contains("Invalid username or password") || e.getMessage().contains("account migrated"))
                PentiumPlus.LOG.error("Wrong password.");
            else PentiumPlus.LOG.error("Failed to contact the authentication server.");
            return false;
        }
    }

    public YggdrasilUserAuthentication getAuth() {
        YggdrasilUserAuthentication auth = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(((MinecraftClientAccessor) mc).getProxy(), "").createUserAuthentication(Agent.MINECRAFT);

        auth.setUsername(name);
        auth.setPassword(password);

        return auth;
    }

    @Override
    public NbtCompound toTag() {
        NbtCompound tag = super.toTag();

        tag.putString("password", password);

        return tag;
    }

    @Override
    public PremiumAccount fromTag(NbtCompound tag) {
        super.fromTag(tag);
        if (!tag.contains("password")) throw new NbtException();

        password = tag.getString("password");

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PremiumAccount)) return false;
        return ((PremiumAccount) o).name.equals(this.name);
    }
}
