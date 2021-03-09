package org.screamingsandals.lib.utils.adventure;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.MessageType;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.SoundStop;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.screamingsandals.lib.utils.reflect.InstanceMethod;
import org.screamingsandals.lib.utils.reflect.Reflect;

@UtilityClass
public class AdventureUtils {
    public final Class<?> NATIVE_MESSAGE_TYPE_CLASS
            = Reflect.getClassSafe("net.kyori.adventure.audience.MessageType");

    public InstanceMethod get(Object instance, String method, Class<?>... types) {
        var classes = new Class<?>[types.length];
        for (var i = 0; i < classes.length; i++) {
            if (Component.class.isAssignableFrom(types[i])) {
                types[i] = ComponentUtils.NATIVE_COMPONENT_CLASS;
            } else if (Title.Times.class.isAssignableFrom(types[i])) {
                types[i] = TitleUtils.NATIVE_TIMES_CLASS;
            } else if (Title.class.isAssignableFrom(types[i])) {
                types[i] = TitleUtils.NATIVE_TITLE_CLASS;
            } else if (Book.class.isAssignableFrom(types[i])) {
                types[i] = BookUtils.NATIVE_BOOK_CLASS;
            } else if (Identity.class.isAssignableFrom(types[i])) {
                types[i] = IdentityUtils.NATIVE_IDENTITY_CLASS;
            } else if (MessageType.class.isAssignableFrom(types[i])) {
                types[i] = NATIVE_MESSAGE_TYPE_CLASS;
            } else if (Key.class.isAssignableFrom(types[i])) {
                types[i] = KeyUtils.NATIVE_KEY_CLASS;
            } else if (Sound.class.isAssignableFrom(types[i])) {
                types[i] = SoundUtils.NATIVE_SOUND_CLASS;
            } else if (Sound.Source.class.isAssignableFrom(types[i])) {
                types[i] = SoundUtils.NATIVE_SOURCE_CLASS;
            } else if (SoundStop.class.isAssignableFrom(types[i])) {
                types[i] = SoundUtils.NATIVE_SOUND_STOP_CLASS;
            }
            classes[i] = types[i];
        }

        return Reflect
                .getMethod(instance, method, classes)
                .withTransformer((parameterTypes, parameters) -> {
                    var result = new Object[parameters.length];
                    for (var i = 0; i < result.length; i++) {
                        if (ComponentUtils.NATIVE_COMPONENT_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = ComponentUtils.componentToPlatform((Component) parameters[i]);
                        } else if (TitleUtils.NATIVE_TIMES_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = TitleUtils.timesToPlatform((Title.Times) parameters[i]);
                        } else if (TitleUtils.NATIVE_TITLE_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = TitleUtils.titleToPlatform((Title) parameters[i]);
                        } else if (BookUtils.NATIVE_BOOK_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = BookUtils.bookToPlatform((Book) parameters[i]);
                        } else if (IdentityUtils.NATIVE_IDENTITY_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = IdentityUtils.identityToPlatform((Identity) parameters[i]);
                        } else if (NATIVE_MESSAGE_TYPE_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = Reflect.findEnumConstant(NATIVE_MESSAGE_TYPE_CLASS, ((MessageType) parameters[i]).name());
                        } else if (KeyUtils.NATIVE_KEY_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = KeyUtils.keyToPlatform((Key) parameters[i]);
                        } else if (SoundUtils.NATIVE_SOUND_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = SoundUtils.soundToPlatform((Sound) parameters[i]);
                        } else if (SoundUtils.NATIVE_SOURCE_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = SoundUtils.sourceToPlatform((Sound.Source) parameters[i]);
                        } else if (SoundUtils.NATIVE_SOUND_STOP_CLASS.isAssignableFrom(parameterTypes[i])) {
                            result[i] = SoundUtils.stopSoundToPlatform((SoundStop) parameters[i]);
                        } else {
                            result[i] = parameters[i];
                        }
                    }
                    return result;
                });
    }
}
